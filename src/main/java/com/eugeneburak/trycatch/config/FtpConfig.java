package com.eugeneburak.trycatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

@Configuration
public class FtpConfig {

  @Value("${ftp.server.host}")
  private String ftpHost;

  @Value("${ftp.server.port}")
  private Integer ftpPort;

  @Value("${ftp.server.user}")
  private String ftpUser;

  @Value("${ftp.server.password}")
  private String ftpPassword;

  private static final int ENTER_LOCAL_PASSIVE_MODE = 2;

  private static final String ENCODING = "UTF-8";

  /**
   * Creates and configure Bean SessionFactory.
   *
   * @return custom SessionFactory.
   */
  @Bean
  public DefaultFtpSessionFactory ftpSessionFactory() {
    DefaultFtpSessionFactory sessionFactory = new DefaultFtpSessionFactory();
    sessionFactory.setHost(ftpHost);
    sessionFactory.setPort(ftpPort);
    sessionFactory.setUsername(ftpUser);
    sessionFactory.setPassword(ftpPassword);
    sessionFactory.setClientMode(ENTER_LOCAL_PASSIVE_MODE);
    sessionFactory.setControlEncoding(ENCODING);
    return sessionFactory;
  }
}
