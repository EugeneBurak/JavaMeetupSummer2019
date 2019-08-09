package com.eugeneburak.trycatch.dto;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class CardCreatedOrUpdatedDto {
  private String mail;
  private String created;

  /**
   * Custom method - Represents a class in JSON String format.
   *
   * @return String.
   */
  public String toJson() {
    return new Gson().toJson(this);
  }
}
