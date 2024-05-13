package com.onetuks.dbstorage.config;

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
@ConfigurationProperties(prefix = "spring.jpa")
public class JpaPropertyConfig {

  private Hibernate hibernate;
  private boolean showSql;
  private boolean openInView;

  @Getter
  @Setter
  @ToString
  @RefreshScope
  @Configuration
  @ConfigurationProperties(prefix = "spring.jpa.hibernate")
  public static class Hibernate {

    private String ddlAuto;
  }
}
