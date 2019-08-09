package com.eugeneburak.trycatch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class HttpError {
  private String date;
  private HttpStatus status;
  private String message;
}
