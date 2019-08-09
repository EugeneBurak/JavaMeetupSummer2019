package com.eugeneburak.trycatch.config;

import java.util.Collections;
import lombok.val;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;

@Configuration
public class SqsConfig {
  /**
   * Creates and configure Bean for custom setting of SQS listener container.
   *
   * @return configured handler for SQS listener container.
   */
  @Bean
  public QueueMessageHandlerFactory queueMessageHandlerFactory() {
    val container = new QueueMessageHandlerFactory();
    val messageConverter = new MappingJackson2MessageConverter();
    messageConverter.setStrictContentTypeMatch(false);
    container.setArgumentResolvers(
        Collections.singletonList(
            new PayloadArgumentResolver(messageConverter)));
    return container;
  }
}
