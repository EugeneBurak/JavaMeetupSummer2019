package com.eugeneburak.trycatch.service;

import com.eugeneburak.trycatch.dto.CardCreatedOrUpdatedDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log
@Service
public class TryCatchUserPointService {

  @Value("${aws.sqs.confirm-points.name}")
  private String queueName;

  private static final String INCORRECT_DATA = "incorrect input data";

  /**
   * The method extract UserPoint data from sqsMessage.
   *
   * @param sqsMessage This is sqs message.
   */
  public Optional<CardCreatedOrUpdatedDto> extractUserPoint(Map<String, Object> sqsMessage) {
    return Optional.ofNullable(sqsMessage)
        .map(message -> message.get("Message"))
        .map(
            messageBody -> {
              try {
                return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"))
                    .readValue((String) messageBody, CardCreatedOrUpdatedDto.class);
              } catch (IOException e) {
                log.warning(INCORRECT_DATA + e.getMessage());
                return null;
              }
            });
  }
}
