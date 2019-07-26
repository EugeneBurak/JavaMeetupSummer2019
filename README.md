# TryCatch
Service for Java Meetup summer 2019. 

## Getting Started
Clone project from repository using git. 
After that you can open it in your favorite IDE.

### Prerequisites
Before start, please make sure that you installed:
 
* [java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Programing language 
* [Maven](https://maven.apache.org/) - Dependency Management (use latest)
* [Docker Compose](https://docs.docker.com/compose/) - Compose is a tool for defining and running multi-container Docker applications.
* run `java -version`
* run `mvn -version`
* run `docker -v`


The project provides containerized in-memory implementation of the dynamoDB, so you don't need to install it locally. But if it is necessary to hack into dynamoDB 
the best option is to  use Docker dynamoDB container.

* [DynamoDB](https://aws.amazon.com/dynamodb/) - dynamoDB from AWS

The project provides containerized in-memory implementation of the Amazon Simple Queue Service (AWS-SQS) for local testing.

* [SQS](https://aws.amazon.com/ru/sqs/) - SQS from AWS
* [DockerizedSQS](https://hub.docker.com/r/roribio16/alpine-sqs/) - Dockerized ElasticMQ server + web UI over Alpine Linux for local development

From the root directory (in the same folder where pom.xml file is located) you need to run
`mvn package`

After that, project will be tested and built and you can start it with command
`docker-compose up`

 You can run a Spring Boot application from your IDE as a simple Java application. However, firstly you need to import your project.
 Import steps depends on your IDE and build system. Most IDEs can import Maven projects directly. 
 For example:
 
  * Intellij IDEA users can select File → New → Project from Existing Sources... → Maven project file (pom.xml)
  * Eclipse users can select Import…​ → Existing Maven Projects from the File menu.

Before running test directly from IDE, please, make sure that you run
`mvn validate` 

It will create all necessary folders that needs for proper run of inmemory dymanoDB.

Additional maven commands:

* clean: cleans up artifacts created by prior builds
* validate: validate the project is correct and all necessary information is available
* compile: compile the source code of the project
* test: test the compiled source code using a suitable unit testing framework. These tests should not require the code be packaged or deployed
* package: take the compiled code and package it in its distributable format, such as a JAR.
* integration-test: process and deploy the package if necessary into an environment where integration tests can be run
* verify: run any checks to verify the package is valid and meets quality criteria
* install: install the package into the local repository, for use as a dependency in other projects locally 
* deploy: done in an integration or release environment, copies the final package to the remote repository for sharing with other developers and projects.

Additional links:

* [Spring Boot starter](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html)
* [Spring data DynamoDB](https://github.com/derjust/spring-data-dynamodb)

## Running the tests
It is vital to use Spring Profiles with tests. Spring Profiles provide a way to segregate parts of your application configuration 
and make it available only in certain environments. Any @Component or @Configuration can be marked with @Profile to 
limit when it is loaded, as shown in the following example:

```
@Configuration
@Profile("production")
public class ProductionConfiguration {

	// ...

 } 
```
All tests which use in-memory dynamoDb should be started with dev profile.If you want to connect to local dynamoDB,
first what you need is to create different profile with your env settings. You can create your own test config with 
dev profile init or extend "AbstractTest" class. Example:

```
 @RunWith(SpringRunner.class)
 @SpringBootTest(classes = InitializationConfig.class)
 @ActiveProfiles("dev")
 public abstract class AbstractTest { 
 }
```

[Spring profile](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html) - Get to know Spring Profiles better

### Coding style tests
Please use this code style if possible [code style](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf)

## Feature Toggles naming convention
Each FT name should match to fragment's file name by the convention: FTName = 'trycatch_' + 
'{any ft description, words are separated with '-' symbol }' + 
FileName{without file extension, words are separated with '-'}

Example:
'points-obtained.html' --> 'trycatch_please-display_points-obtained'
Then feature-toggles will be loaded and linked to according fragments automatically after 
microservise started.

## Deployment
All deployments are done by [Bitbucket](ttps://bitbucket.org)
Please be sure that all tests run well, in other case build will fail.

## Built With

* [Spring Data DynamoDB](https://github.com/derjust/spring-data-dynamodb/wiki) - Used to connect to dynamoDB

## Local testing

* install [AWS Command Line Interface](https://docs.aws.amazon.com/en_us/cli/latest/userguide/cli-chap-install.html)
* all the scripts you run in the IDEA terminal or CMD console
* verify that the AWS CLI installed correctly by running: 
* run `aws --version` command  
* correct response: `aws-cli/version Python/version OS/version botocore/version` 
* run `mvn clean` - not necessary
* run `mvn package`
* run `docker-compose up` command

## Local an application testing

* after all services started point your web browser to `http://localhost:8080/health`. You should see the message "I am alive!".
* you can test application use next endpoints: 
    `http://localhost:8080/trycatch/api` - for testing DB - use CRUD.
    `http://localhost:8080/trycatch/calculate?money-spent=moneySpentAmountValue`
    `http://localhost:8080/trycatch/validate?card-number=tryCatchCurdNumber`
    
## Local SQS testing

* after all services started point your web browser to http://localhost:9325. You should see the web UI for SQS
* probably you need to explicitly set aws settings before messaging. For that run:
  `aws configure` 
  and set following fields:
    `AWS Access Key ID: key`
    `AWS Secret Access Key: key2`
    `Default region name: eu-west-1`
    `Default output format: None`
* to send a message to the queue confirm-points run:
  `aws --endpoint-url http://localhost:9324 sqs send-message --queue-url http://localhost:9324/queue/confirm-points --message-body 
  "{\"Message\":{\"orderCreated\":\"Tue Jan 15 11:43:10 CET 2019\",\"orderTotalSum\":\"9.94\",\"orderId\":\"201334328\"}}"`
* to send a message to the queue upload-points (to trigger flow) run: 
  `aws --endpoint-url http://localhost:9324 sqs send-message --queue-url http://localhost:9324/queue/upload-points --message-body "Message"`  

With love, your TryCatch team!
