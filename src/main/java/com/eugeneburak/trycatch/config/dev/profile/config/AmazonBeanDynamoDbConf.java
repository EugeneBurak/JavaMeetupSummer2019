package com.eugeneburak.trycatch.config.dev.profile.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.eugeneburak.trycatch.config.profiles.ProfileDev;
import lombok.val;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@ProfileDev
@Configuration
@EnableDynamoDBRepositories(
    dynamoDBMapperConfigRef = "dynamoDbMapperConfig",
    basePackages = "com.eugeneburak.trycatch.repository",
    mappingContextRef = "dynamoDbMappingContext")
public class AmazonBeanDynamoDbConf {

  @Value("${amazon.dynamodb.endpoint}")
  private String amazonDynamoDbEndpoint;

  @Value("${amazon.dynamodb.port}")
  private String amazonDynamoDbPort;

  @Value("${amazon.aws.region}")
  private String amazonDynamoDbRegion;

  @Value("${amazon.aws.accesskey}")
  private String amazonAwsAccessKey;

  @Value("${amazon.aws.secretkey}")
  private String amazonAwsSecretKey;

  @Value("${amazon.dynamodb.table.prefix}")
  private String tablePrefix;

  /**
   * create an object that allow to interact with DynamoDb.
   *
   * @param staticCredentialsProvider credentials provider with defined credentials
   * @return client for DynamoDb
   */
  @Bean(name = "amazonDynamoDB")
  public AmazonDynamoDB amazonDynamoDb(AWSStaticCredentialsProvider staticCredentialsProvider) {
    val fullEndPoint = amazonDynamoDbEndpoint + ":" + this.amazonDynamoDbPort + "/";
    AwsClientBuilder.EndpointConfiguration endpointConfiguration =
        new AwsClientBuilder.EndpointConfiguration(fullEndPoint, this.amazonDynamoDbRegion);

    return AmazonDynamoDBClientBuilder.standard()
        .withEndpointConfiguration(endpointConfiguration)
        .withCredentials(staticCredentialsProvider)
        .build();
  }

  /**
   * Add prefix to table name according to environment.
   *
   * @return mapper with defined config
   */
  @Bean
  public DynamoDBMapperConfig dynamoDbMapperConfig() {
    val builder = new DynamoDBMapperConfig.Builder();
    builder.setTableNameOverride(TableNameOverride.withTableNamePrefix(tablePrefix));
    return new DynamoDBMapperConfig(DynamoDBMapperConfig.DEFAULT, builder.build());
  }

  /**
   * Configure dynamoDbMapper.
   *
   * @param amazonDynamoDb our AmazonDynamoDB
   * @param config our custom DynamoDBMapperConfig
   * @return mapper with defined config
   */
  @Bean
  public DynamoDBMapper dynamoDbMapper(AmazonDynamoDB amazonDynamoDb, DynamoDBMapperConfig config) {
    return new DynamoDBMapper(amazonDynamoDb, config);
  }

  /**
   * Create object for credentials.
   *
   * @return credentials provider with defined credentials
   */
  @Bean
  @Primary
  public AWSStaticCredentialsProvider amazonAwsCredentialsProvider() {
    return new AWSStaticCredentialsProvider(
        new BasicAWSCredentials(this.amazonAwsAccessKey, this.amazonAwsSecretKey));
  }

  @Bean
  public DynamoDBMappingContext dynamoDbMappingContext() {
    return new DynamoDBMappingContext();
  }
}
