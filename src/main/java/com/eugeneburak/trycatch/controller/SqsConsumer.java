package com.eugeneburak.trycatch.controller;

import com.eugeneburak.trycatch.config.profiles.ProfileNotDev;
import com.eugeneburak.trycatch.service.TryCatchUserPointService;
import java.util.Map;

import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Log
@Component
@ProfileNotDev
@Profile("local")
public class SqsConsumer {
  /**
   * TryCatchUserPointService - saves the data in the database or logs the message in case of
   * failure.
   */
  private final TryCatchUserPointService tryCatchUserPointService;

  @Autowired
  public SqsConsumer(TryCatchUserPointService tryCatchUserPointService) {
    this.tryCatchUserPointService = tryCatchUserPointService;
  }

  /**
   * Consumes and persists 'order created event' messages.
   *
   * @param confirmPoints contains TryCatch - specific data from created order.
   */
  @SqsListener("${aws.sqs.confirm-points.name}")
  public void confirmPointsListener(Map<String, Object> confirmPoints) {
    val minMoneyAmount = 5;
    tryCatchUserPointService.extractUserPoint(confirmPoints);
  }
}
