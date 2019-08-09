package com.eugeneburak.trycatch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TryCatchAmount {
  private String timeStamp;
  private String money;
  private Integer tryCatchPoints;
}
