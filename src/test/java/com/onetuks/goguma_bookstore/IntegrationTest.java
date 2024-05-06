package com.onetuks.goguma_bookstore;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@Ignore
@SpringBootTest
@Transactional
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {

  static final ComposeContainer cloudConfig;

  static {
    cloudConfig =
        new ComposeContainer(
                new File(
                    "/Users/onetuks/Documents/CodeSpace/projects/goguma-bookstore/goguma-bookstore-server/db/test/docker-compose.yaml"))
            .withExposedService(
                "cloud-config",
                8888,
                Wait.forLogMessage(".*ready for connections.*", 1)
                    .withStartupTimeout(Duration.ofSeconds(300)));

    cloudConfig.start();
  }

  static class IntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      properties.put("spring.application.name", "goguma");
      properties.put("spring.profiles.active", "dev");

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
