package org.opal;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import io.vavr.collection.Seq;

public abstract class BadRequestException extends DomainExceptionWithErrors {

  BadRequestException(String reason) {
    this(reason, null, null);
  }

  BadRequestException(String objectName, String reason) {
    this(reason, null, null);
  }

  BadRequestException(String reason, String objectName, Seq<DomainError> errors) {
    super(BAD_REQUEST, reason, objectName, errors);
  }
  
}

