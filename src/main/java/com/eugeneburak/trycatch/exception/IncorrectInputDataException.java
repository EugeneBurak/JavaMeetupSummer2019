package com.eugeneburak.trycatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class IncorrectInputDataException extends RuntimeException {
  public IncorrectInputDataException(String message) {
    super(message);
  }
}
