package org.opal;

import io.vavr.collection.Seq;

class InvalidAddressException extends UnprocessableEntityException {

  InvalidAddressException(String reason) {
    this(reason, null, null);
  }

  InvalidAddressException(String reason, String objectName) {
    this(reason, objectName, null);
  }

  InvalidAddressException(String reason, String objectName, Seq<DomainError> errors) {
    super(reason, objectName, errors);
  }

}
