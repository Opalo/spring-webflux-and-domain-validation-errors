package org.opal;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("addresses")
class AddressController {

  private final NewAddressValidator validator;

  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  @ResponseBody
  NewAddressResponse post(@Valid @RequestBody NewAddressRequest req) {
    return Try.of(() -> req)
      .flatMap(this::validate)
      .map(this::toResponse)
      .get();
  }

  Try<NewAddressRequest> validate(NewAddressRequest req) {
    return validator.validate(req);
  }

  NewAddressResponse toResponse(NewAddressRequest req) {
    return new NewAddressResponse(randomUUID(), req.addressLine1, req.addressLine2, req.addressLine3, req.zip, req.city, req.province, req.country);
  }

}
