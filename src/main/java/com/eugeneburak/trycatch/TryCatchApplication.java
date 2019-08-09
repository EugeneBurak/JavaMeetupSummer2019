package com.eugeneburak.trycatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Import({com.eugeneburak.trycatch.config.SwaggerConfig.class})
@IntegrationComponentScan
@EnableIntegration
public class TryCatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(TryCatchApplication.class, args);
  }
}
