package com.eugeneburak.trycatch.controller;

import com.eugeneburak.trycatch.exception.IncorrectInputDataException;
import com.eugeneburak.trycatch.model.TryCatchAmount;
import com.eugeneburak.trycatch.service.TryCatchPointCalculationService;
import com.eugeneburak.trycatch.service.TryCatchValidationService;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/trycatch")
public class TryCatchController {

  private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

  /**
   * TryCatchPointCalculationService - converts the amount of money spent by the user in TryCatch
   * points - rewards.
   */
  @Setter private TryCatchPointCalculationService tryCatchPointCalculationService;

  /** TryCatchValidationService - checks if the user TryCatch card is valid. */
  private final TryCatchValidationService tryCatchValidationService;

  /** Constructor. */
  @Autowired
  public TryCatchController(
          TryCatchPointCalculationService tryCatchPointCalculationService,
          TryCatchValidationService tryCatchValidationService) {
    this.tryCatchPointCalculationService = tryCatchPointCalculationService;
    this.tryCatchValidationService = tryCatchValidationService;
  }

  /** The controller returns TryCatch points. */
  @GetMapping(path = "/calculate")
  public TryCatchAmount tryCatchCalculations(@RequestParam(name = "money-spent") String moneySpent)
      throws IncorrectInputDataException {
    return TryCatchAmount.builder()
        .timeStamp((new SimpleDateFormat(DATE_FORMAT)).format(new Date()))
        .money(moneySpent)
        .tryCatchPoints(this.tryCatchPointCalculationService.tryCatchPointCalculation(moneySpent))
        .build();
  }

  /** The controller returns cardNumberValidate result - status 200 or 422. */
  @GetMapping(path = "/validate")
  public void tryCatchCardValidations(@RequestParam(name = "card-number") String cardNumber)
      throws IncorrectInputDataException {
    this.tryCatchValidationService.cardNumberValidate(cardNumber);
  }
}
