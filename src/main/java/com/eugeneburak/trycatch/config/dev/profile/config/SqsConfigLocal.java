package com.eugeneburak.trycatch.config.dev.profile.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import java.net.URISyntaxException;
import lombok.val;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
public class SqsConfigLocal {

  @Value("${aws.sqs.host}")
  private String sqsHost;

  @Value("${aws.sqs.port}")
  private int sqsPort;

  @Value("${amazon.aws.region}")
  private String region;

  @Autowired private AWSStaticCredentialsProvider credentialsProvider;

  /**
   * Creates and configure Bean for custom setting of SQS listener container.
   *
   * @return configured SQS listener container factory.
   */
  @Bean
  public SimpleMessageListenerContainerFactory listenerContainerFactory(
      AmazonSQSAsync amazonSqsAsync) {
    val container = new SimpleMessageListenerContainerFactory();
    container.setAmazonSqs(amazonSqsAsync);
    return container;
  }

  /**
   * Creates and configure Bean for accessing Amazon SQS asynchronously.
   *
   * @return asynchronous client for SQS
   */
  @Bean
  @Primary
  public AmazonSQSAsync amazonSqsAsyncInit() throws URISyntaxException {
    val serviceEndpoint =
        new URIBuilder().setScheme("http").setHost(sqsHost).setPort(sqsPort).build().toString();
    return AmazonSQSAsyncClient.asyncBuilder()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, region))
        .withCredentials(this.credentialsProvider)
        .build();
  }
}
