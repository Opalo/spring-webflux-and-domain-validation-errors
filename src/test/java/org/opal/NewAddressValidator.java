package org.opal;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;
import static io.vavr.control.Validation.combine;
import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;
import static java.lang.String.format;
import static org.opal.HasDomainErrors.DomainFieldError.error;

import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.springframework.stereotype.Component;

@Component
class NewAddressValidator {

  Try<NewAddressRequest> validate(NewAddressRequest req) {
    final var validation = combine(validateCountry(req.country), validateZip(req.zip, req.province))
      .ap((country, zip) -> req)
      .mapError(errors -> new InvalidAddressException("Address is invalid", NewAddressRequest.class.getName(), errors));
    return validation.isValid() ? success(validation.get()) : failure(validation.getError());
  }


  private Validation<HasDomainErrors.DomainError, String> validateCountry(String country) {
    return "Molvania".equalsIgnoreCase(country) ?
      invalid(
        error()
          .field("country")
          .code("address.invalid.country")
          .message(format("Country: '%s' is invalid. It's non-EU country.", country))
          .rejectedValue(country)
          .arguments(new String[]{country})
          .build()
      )
      :
      valid(country);
  }

  private Validation<HasDomainErrors.DomainError, String> validateZip(String zip, String province) {
    final var zip2FirstLetters = zip.substring(0, 2);
    final var province2FirstLetters = province.substring(0, 2);
    return !zip2FirstLetters.equalsIgnoreCase(province2FirstLetters) ?
      invalid(
        error()
          .field("zip")
          .code("address.invalid.zip")
          .message(format("Zip: '%s' is invalid. It does not match the province: '%s'.", zip, province))
          .rejectedValue(zip)
          .arguments(new String[]{zip, province})
          .build()
      )
      :
      valid(zip);
  }

}
