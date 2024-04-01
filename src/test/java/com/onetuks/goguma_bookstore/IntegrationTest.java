package com.onetuks.goguma_bookstore;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.junit.Ignore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@Ignore
@SpringBootTest
@Transactional
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {

  static DockerComposeContainer rdbms;

  static {
    rdbms =
        new DockerComposeContainer(new File("db/test/docker-compose.yaml"))
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

    rdbms.start();
  }

  static class IntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var rdbmsHost = rdbms.getServiceHost("local-db", 3306);
      var rdbmsPort = rdbms.getServicePort("local-db", 3306);

      properties.put(
          "spring.datasource.url",
          "jdbc:mysql://" + rdbmsHost + ":" + rdbmsPort + "/goguma-bookstore");

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
