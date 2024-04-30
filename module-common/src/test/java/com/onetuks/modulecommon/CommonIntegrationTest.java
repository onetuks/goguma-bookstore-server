package com.onetuks.modulecommon;

import com.onetuks.modulecommon.CommonIntegrationTest.CommonIntegrationTestInitializer;
import com.onetuks.modulecommon.util.TestFileCleaner;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@Ignore
@SpringBootTest
@Transactional
@ContextConfiguration(initializers = CommonIntegrationTestInitializer.class)
public class CommonIntegrationTest {

  static final ComposeContainer rdbms;
  static final LocalStackContainer aws;

  @Autowired private TestFileCleaner testFileCleaner;

  @AfterEach
  void tearDown() {
    testFileCleaner.deleteAllTestStatic();
  }

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

    aws =
        new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withServices(Service.S3)
            .withStartupTimeout(Duration.ofSeconds(600));

    rdbms.start();
    aws.start();
  }

  static class CommonIntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var rdbmsHost = rdbms.getServiceHost("local-db", 3306);
      var rdbmsPort = rdbms.getServicePort("local-db", 3306);

      properties.put(
          "spring.datasource.url",
          "jdbc:mysql://" + rdbmsHost + ":" + rdbmsPort + "/goguma-bookstore");
      properties.put("spring.datasource.password", "root1234!");

      try {
        aws.execInContainer("awslocal", "s3api", "create-bucket", "--bucket", "test-bucket");

        properties.put("aws.endpoint", String.valueOf(aws.getEndpoint()));
        properties.put("aws.bucket-name", "test-bucket");
      } catch (Exception e) {
        log.info("aws test initialize failed");
      }

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
