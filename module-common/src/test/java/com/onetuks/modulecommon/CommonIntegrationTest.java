package com.onetuks.modulecommon;

import com.onetuks.modulecommon.CommonIntegrationTest.CommonIntegrationTestInitializer;
import com.onetuks.modulecommon.util.TestFileCleaner;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles(value = "test")
@SpringBootTest
@Transactional
@ContextConfiguration(initializers = CommonIntegrationTestInitializer.class)
public class CommonIntegrationTest {

  static final ComposeContainer containers;
  static final LocalStackContainer localStack;

  private static final int LOCAL_DB_PORT = 3306;
  private static final int LOCAL_DB_MIGRATION_PORT = 0;
  private static final int DURATION = 300;
  private static final String DOCKER_COMPOSE_PATH =
      System.getProperty("rootDir") + "/db/test/docker-compose.yaml";

  @Autowired private TestFileCleaner testFileCleaner;

  @AfterEach
  void tearDown() {
    testFileCleaner.deleteAllTestStatic();
  }

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

    localStack =
        new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withServices(Service.S3)
            .withStartupTimeout(Duration.ofSeconds(600));
    localStack.start();
  }

  static class CommonIntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var localDbHost = containers.getServiceHost("local-db", LOCAL_DB_PORT);
      var localDbPort = containers.getServicePort("local-db", LOCAL_DB_PORT);

      properties.put(
          "spring.datasource.url",
          "jdbc:mysql://" + localDbHost + ":" + localDbPort + "/goguma-bookstore-test");

      try {
        localStack.execInContainer("awslocal", "s3api", "create-bucket", "--bucket", "test-bucket");

        properties.put("aws.endpoint", String.valueOf(localStack.getEndpoint()));
        properties.put("aws.bucket-name", "test-bucket");
      } catch (Exception e) {
        log.info("aws test initialize failed");
      }

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
