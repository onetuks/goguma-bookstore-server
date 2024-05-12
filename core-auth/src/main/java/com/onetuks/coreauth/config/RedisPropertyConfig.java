package com.onetuks.coreauth.config;

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
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisPropertyConfig {

  private String host;
  private int port;
}
