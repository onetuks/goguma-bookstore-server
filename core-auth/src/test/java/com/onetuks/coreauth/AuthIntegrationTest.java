package com.onetuks.coreauth;

import com.onetuks.coreauth.AuthIntegrationTest.AuthIntegrationTestInitializer;
import com.redis.testcontainers.RedisContainer;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@ActiveProfiles(value = "test")
@SpringBootTest
@Transactional
@ContextConfiguration(initializers = AuthIntegrationTestInitializer.class)
public class AuthIntegrationTest {

  static final ComposeContainer containers;
  static final RedisContainer redis;

  private static final int LOCAL_DB_PORT = 3306;
  private static final int LOCAL_DB_MIGRATION_PORT = 0;
  private static final int DURATION = 300;
  private static final String DOCKER_COMPOSE_PATH =
      System.getProperty("rootDir") + "/db/test/docker-compose.yaml";

  static {
    containers =
        new ComposeContainer(new File(DOCKER_COMPOSE_PATH))
            .withExposedService(
                "local-db",
                LOCAL_DB_PORT,
                Wait.forLogMessage(".*ready for connections.*", 1)
                    .withStartupTimeout(Duration.ofSeconds(DURATION)))
            .withExposedService(
                "local-db-migrate",
                LOCAL_DB_MIGRATION_PORT,
                Wait.forLogMessage("(.*Successfully applied.*)|(.*Successfully validated.*)", 1)
                    .withStartupTimeout(Duration.ofSeconds(DURATION)));
    containers.start();

    redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("6"));
    redis.start();
  }

  static class AuthIntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var localDbHost = containers.getServiceHost("local-db", LOCAL_DB_PORT);
      var localDbPort = containers.getServicePort("local-db", LOCAL_DB_PORT);

      properties.put(
          "spring.datasource.url",
          "jdbc:mysql://" + localDbHost + ":" + localDbPort + "/goguma-bookstore-test");

      var redistHost = redis.getHost();
      var redistPort = redis.getFirstMappedPort();

      properties.put("spring.data.redis.host", redistHost);
      properties.put("spring.data.redis.port", String.valueOf(redistPort));

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
