package com.eugeneburak.trycatch.service;

import com.eugeneburak.trycatch.exception.IncorrectInputDataException;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TryCatchPointCalculationService {

  private static final String EXCEPTION_MESSAGE =
      "String amountSpent - must be a positive number! Received string - ";
  private static final int COEFFICIENT = 5;

  /**
   * The method converts the amount of money spent by the user in TryCatch points. If it possible.
   * If the conversion is not possible, returns an error.
   *
   * @param amountSpentByUser This is the amount of money spent by the user.
   * @return tryCatch points - reward.
   * @throws IncorrectInputDataException Occurs when invalid data is found at the input. String
   *     amountSpent - must be a positive number.
   */
  public int tryCatchPointCalculation(String amountSpentByUser) throws IncorrectInputDataException {
    int amountSpent = validationAndConversion(amountSpentByUser);
    if (amountSpent >= COEFFICIENT) {
      return amountSpent / COEFFICIENT;
    }
    return 0;
  }

  private int validationAndConversion(String amountSpent) throws IncorrectInputDataException {
    return Optional.ofNullable(amountSpent)
        .map(String::trim)
        .filter(s -> s.matches("\\d+(\\.\\d+)?"))
        .map(s -> Integer.parseInt((s.split("\\.")[0])))
        .orElseThrow(() -> new IncorrectInputDataException(EXCEPTION_MESSAGE + amountSpent));
  }
}
