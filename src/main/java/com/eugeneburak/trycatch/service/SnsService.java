package com.eugeneburak.trycatch.service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.eugeneburak.trycatch.dto.CardCreatedOrUpdatedDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SnsService {

  private final String name;
  private final AmazonSNSClient snsClient;

  public SnsService(
      @Value("${aws.sns.card-created-or-updated.arn}") String name,
      final AmazonSNSClient snsClient) {
    this.name = name;
    this.snsClient = snsClient;
  }

  /**
   * The method sends a message by SNS card-created-or-updated topic.
   *
   * @param card - CardCreatedOrUpdatedDto.
   */
  public void notifyCardCreatedOrUpdated(CardCreatedOrUpdatedDto card) {
    snsClient.publish(new PublishRequest(name, card.toJson()));
  }
}
