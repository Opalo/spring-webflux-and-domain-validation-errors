package org.opal;

import static org.springframework.http.HttpStatus.CONFLICT;

public abstract class ConflictException extends DomainException {

  public ConflictException(String reason) {
    super(CONFLICT, reason);
  }

}
