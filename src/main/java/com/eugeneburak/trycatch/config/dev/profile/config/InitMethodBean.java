package com.eugeneburak.trycatch.config.dev.profile.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.eugeneburak.trycatch.model.TryCatchUser;
import com.eugeneburak.trycatch.model.TryCatchUserPoint;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
class InitMethodBean implements ApplicationListener<ApplicationReadyEvent> {

  private static final String USER_POINT = "dev_trycatch_user_point";
  private static final String USER = "dev_trycatch_user_card";

  @Autowired private AmazonDynamoDB amazonDynamoDb;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    createDynamoDbBasicTable(TryCatchUserPoint.class, USER_POINT);
    createDynamoDbTableUser(TryCatchUser.class, USER);
  }

  /**
   * Create table in local dynamodb.
   *
   * @param tableClass that need to be created in local dynamoDb.
   * @param tableName is of table
   */
  private void createDynamoDbBasicTable(Class<?> tableClass, String tableName) {
    val mapper =
            new DynamoDBMapper(this.amazonDynamoDb, new DynamoDBMapperConfig.Builder().build());
    val tableRequest = mapper.generateCreateTableRequest(tableClass);
    tableRequest.setTableName(tableName);
    tableRequest.setProvisionedThroughput(new ProvisionedThroughput(2L, 2L));
    this.amazonDynamoDb.createTable(tableRequest);
  }

  /**
   * Create table in local dynamodb.
   *
   * @param tableClass that need to be created in local dynamoDb.
   * @param tableName is of table
   */
  private void createDynamoDbTableUser(Class<?> tableClass, String tableName) {
    val mapper =
        new DynamoDBMapper(this.amazonDynamoDb, new DynamoDBMapperConfig.Builder().build());
    val tableRequest = mapper.generateCreateTableRequest(tableClass);
    tableRequest.setTableName(tableName);
    tableRequest.setProvisionedThroughput(new ProvisionedThroughput(2L, 2L));
    tableRequest
        .getGlobalSecondaryIndexes()
        .get(0)
        .setProvisionedThroughput(new ProvisionedThroughput(3L, 1L));
    this.amazonDynamoDb.createTable(tableRequest);
  }
}
