package org.opal

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification

import static groovy.json.JsonOutput.toJson
import static jodd.net.HttpMethod.POST
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpStatus.*

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SampleApp)
class ExceptionHandlerSpec extends Specification implements HTTPHelper {

  @LocalServerPort
  int port

  def 'should return 400 and error according to bean validation'() {
    when:
    def response = request().path('/addresses').method(POST).body(toJson([:])).send()

    then:
    response.statusCode() == BAD_REQUEST.value()

    and:
    printJSON(response)

    and:
    def content = fromJSON(response)
    content.size() == 7
    content.timestamp
    content.status == BAD_REQUEST.value()
    content.error == BAD_REQUEST.reasonPhrase
    content.errors.size() == 5
    content.errors*.field.containsAll(['addressLine1', 'zip', 'city', 'province', 'country',])
    content.errors*.defaultMessage.every { it == 'must not be null' }
    content.message.startsWith('Validation failed for argument at index')
    content.path == '/addresses'
    content.traceId
  }

  def 'should return 422 with 2 non bean validation errors'() {
    when:
    def response = request().path('/addresses').method(POST).body(toJson([
          addressLine1: 'al1',
          addressLine2: null,
          addressLine3: null,
          zip         : 'SOME ZIP',
          city        : 'Dat City',
          province    : 'Other Province',
          country     : 'Molvania',
    ])).send()

    then:
    response.statusCode() == UNPROCESSABLE_ENTITY.value()

    and:
    printJSON(response)

    and:
    def content = fromJSON(response)
    content.size() == 7
    content.timestamp
    content.status == UNPROCESSABLE_ENTITY.value()
    content.error == UNPROCESSABLE_ENTITY.reasonPhrase
    content.errors.size() == 2
    content.errors*.field.containsAll(['country', 'zip'])
    content.message == "Address is invalid"

    and:
    def zip = content.errors.find { it.field == 'zip' } as Map
    zip.codes == ['address.invalid.zip']
    zip.arguments == ["SOME ZIP", "Other Province"]
    zip.defaultMessage == "Zip: 'SOME ZIP' is invalid. It does not match the province: 'Other Province'."
    zip.objectName == 'org.opal.NewAddressRequest'
    zip.field == 'zip'
    zip.rejectedValue == 'SOME ZIP'
    !zip.bindinFailure
    zip.code == 'address.invalid.zip'

    and:
    def country = content.errors.find { it.field == 'country' } as Map
    country.codes == ['address.invalid.country']
    country.defaultMessage == "Country: 'Molvania' is invalid. It's non-EU country."
    country.arguments == ['Molvania']
    country.objectName == 'org.opal.NewAddressRequest'
    country.field == 'country'
    country.rejectedValue == 'Molvania'
    !country.bindinFailure
    country.code == 'address.invalid.country'
  }

  def 'should return 200'() {
    def address = [
          addressLine1: 'al1',
          addressLine2: null,
          addressLine3: null,
          zip         : 'Pr ZIP',
          city        : 'Dat City',
          province    : 'Province',
          country     : 'Haxaco',
    ]
    when:
    def response = request().path('/addresses').method(POST).body(toJson(address)).send()

    then:
    response.statusCode() == OK.value()

    and:
    printJSON(response)

    and:
    def content = fromJSON(response)
    content.size() == 8
    content.id
    content.addressLine1 == address.addressLine1
    content.addressLine2 == address.addressLine2
    content.addressLine3 == address.addressLine3
    content.zip == address.zip
    content.city == address.city
    content.province == address.province
    content.country == address.country
  }

}
