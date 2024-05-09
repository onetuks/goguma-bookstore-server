package com.onetuks.modulepersistence.global.config;

import java.sql.Driver;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourcePropertyConfig {

  private Driver driver;
  private String url;
  private String username;
  private String password;
}
