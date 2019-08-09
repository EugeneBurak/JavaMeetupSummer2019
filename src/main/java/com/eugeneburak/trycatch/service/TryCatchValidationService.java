package com.eugeneburak.trycatch.service;

import com.eugeneburak.trycatch.exception.IncorrectInputDataException;
import java.util.Optional;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Log
@Service
public class TryCatchValidationService {

  private static final String NOT_VALID_MESSAGE =
      "tryCatchCardNumber - must have nine digits and be valid - ";

  /**
   * This method checks if the user TryCatch card is valid.
   *
   * @param cardNumber This is the user TryCatch card number.
   * @throws IncorrectInputDataException Occurs when invalid or wrong data is found at the input.
   */
  public void cardNumberValidate(String cardNumber) throws IncorrectInputDataException {
    if (!isCardNumberValid(cardNumber)) {
      throw generateException(cardNumber);
    }
  }

  private boolean isCardNumberValid(String tryCatchCardNumber) throws IncorrectInputDataException {
    return Optional.ofNullable(tryCatchCardNumber)
            .map(String::trim)
            .filter(s -> s.matches("^[0-9]{9}$"))
            .map(s -> isValid(tryCatchCardNumber))
            .orElseThrow(() -> generateException(tryCatchCardNumber));
  }

  private boolean isValid(String cardNumber) {
    int accumulator = 0;
    for (int i = 0; i < cardNumber.length(); i++) {
      accumulator += (Character.getNumericValue(cardNumber.charAt(i)) * (cardNumber.length() - i));
    }
    return accumulator % 11 == 0;
  }

  private IncorrectInputDataException generateException(String message)
      throws IncorrectInputDataException {
    final String errorMessage = NOT_VALID_MESSAGE + message;
    return new IncorrectInputDataException(errorMessage);
  }
}
