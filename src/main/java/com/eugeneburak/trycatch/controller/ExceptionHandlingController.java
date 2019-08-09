package com.eugeneburak.trycatch.controller;

import com.eugeneburak.trycatch.exception.IncorrectInputDataException;
import com.eugeneburak.trycatch.model.HttpError;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
@RestController
public class ExceptionHandlingController {

  /**
   * The method handle our custom exception.
   *
   * @param ex exception
   * @return json object
   */
  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(value = {IllegalArgumentException.class, IncorrectInputDataException.class})
  public HttpError handleBadRequest(Exception ex) {
    log.info(ex.getMessage(), ex);
    return HttpError.builder()
        .date((new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()))
        .status(HttpStatus.UNPROCESSABLE_ENTITY)
        .message(ex.getMessage())
        .build();
  }

  /**
   * The method handle all exception.
   *
   * @param ex exception
   * @return json object
   */
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable.class)
  public HttpError handleException(Exception ex) {
    log.warn(ex.getMessage(), ex);
    return HttpError.builder()
        .date((new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()))
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .message(ex.getMessage())
        .build();
  }
}
