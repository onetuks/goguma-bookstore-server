package com.onetuks.moduleauth;

import com.onetuks.moduleauth.AuthIntegrationTest.AuthIntegrationTestInitializer;
import com.redis.testcontainers.RedisContainer;
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
@ContextConfiguration(initializers = AuthIntegrationTestInitializer.class)
public class AuthIntegrationTest {

  static final ComposeContainer rdbms;
  static final RedisContainer redis;

  static {
    rdbms =
        new ComposeContainer(
                new File(
                    "/Users/onetuks/Documents/CodeSpace/projects/goguma-bookstore/goguma-bookstore-server/db/test/docker-compose.yaml"))
            .withExposedService(
                "local-db",
                3306,
                Wait.forLogMessage(".*ready for connections.*", 1)
                    .withStartupTimeout(Duration.ofSeconds(300)))
            .withExposedService(
                "local-db-migrate",
                0,
                Wait.forLogMessage("(.*Successfully applied.*)|(.*Successfully validated.*)", 1)
                    .withStartupTimeout(Duration.ofSeconds(300)));

    redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("6"));

    rdbms.start();
    redis.start();
  }

  static class AuthIntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var rdbmsHost = rdbms.getServiceHost("local-db", 3306);
      var rdbmsPort = rdbms.getServicePort("local-db", 3306);

      properties.put(
          "spring.datasource.url",
          "jdbc:mysql://" + rdbmsHost + ":" + rdbmsPort + "/goguma-bookstore");
      properties.put("spring.datasource.password", "root1234!");

      var redistHost = redis.getHost();
      var redistPort = redis.getFirstMappedPort();

      properties.put("spring.data.redis.host", redistHost);
      properties.put("spring.data.redis.port", String.valueOf(redistPort));

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
