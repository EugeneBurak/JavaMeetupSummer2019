package com.eugeneburak.trycatch.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.eugeneburak.trycatch.config.profiles.ProfileNotDev;
import lombok.val;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ProfileNotDev
@EnableDynamoDBRepositories(
    dynamoDBMapperConfigRef = "dynamoDbMapperConfig",
    basePackages = "com.eugeneburak.trycatch.repository",
    mappingContextRef = "dynamoDbMappingContext"
)
public class DynamoDbConfig {

  @Value("${amazon.dynamodb.table.prefix}")
  private String tablePrefix;

  @Bean(name = "amazonDynamoDB")
  public AmazonDynamoDB amazonDynamoDb() {
    return AmazonDynamoDBClientBuilder.standard().build();
  }

  /**
   * Add prefix to table name according to environment.
   *
   * @return mapper with defined config
   */
  @Bean
  public DynamoDBMapperConfig dynamoDbMapperConfig() {

    val builder = new DynamoDBMapperConfig.Builder();
    TableNameOverride.withTableNamePrefix(tablePrefix);
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

  @Bean
  public DynamoDBMappingContext dynamoDbMappingContext() {
    return new DynamoDBMappingContext();
  }
}
