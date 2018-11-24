package org.opal;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import io.vavr.collection.Seq;

public abstract class UnprocessableEntityException extends DomainExceptionWithErrors {

  UnprocessableEntityException(String reason) {
    this(reason, null, null);
  }

  UnprocessableEntityException(String objectName, String reason) {
    this(reason, null, null);
  }

  UnprocessableEntityException(String reason, String objectName, Seq<DomainError> errors) {
    super(UNPROCESSABLE_ENTITY, reason, objectName, errors);
  }

}