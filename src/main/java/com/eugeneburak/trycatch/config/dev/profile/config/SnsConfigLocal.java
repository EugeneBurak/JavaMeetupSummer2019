package com.eugeneburak.trycatch.config.dev.profile.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.eugeneburak.trycatch.config.profiles.ProfileDev;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ProfileDev
@Configuration
public class SnsConfigLocal {

  @Value("${aws.sns.endpoint}")
  private String endpoint;

  /**
   * Creates custom AmazonSNSClient to publish messages to a local SNS topic.
   *
   * @param amazonAwsCredentialsProvider our custom AWSStaticCredentialsProvider.
   * @return AmazonSNSClient.
   */
  @Bean
  @Primary
  public AmazonSNSClient amazonSnsClient(
      AWSStaticCredentialsProvider amazonAwsCredentialsProvider) {
    AmazonSNSClient snsClient = new AmazonSNSClient(amazonAwsCredentialsProvider);
    snsClient.setEndpoint(endpoint);
    return snsClient;
  }
}
