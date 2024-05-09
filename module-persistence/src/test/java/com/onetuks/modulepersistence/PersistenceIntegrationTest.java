package com.onetuks.modulepersistence;

import com.onetuks.modulecommon.config.CommonTestBeanProviderConfig;
import com.onetuks.modulecommon.util.TestFileCleaner;
import com.onetuks.modulepersistence.PersistenceIntegrationTest.PersistenceIntegrationTestInitializer;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@SpringBootTest
@Transactional
@ContextConfiguration(
    initializers = PersistenceIntegrationTestInitializer.class,
    classes = {CommonTestBeanProviderConfig.class})
public class PersistenceIntegrationTest {

  static final ComposeContainer containers;

  private static final int LOCAL_DB_PORT = 3306;
  private static final int LOCAL_DB_MIGRATION_PORT = 0;
  private static final int CLOUD_CONFIG_PORT = 8888;
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
                "cloud-config",
                CLOUD_CONFIG_PORT,
                Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(DURATION)))
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
  }

  static class PersistenceIntegrationTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      Map<String, String> properties = new HashMap<>();

      var cloudConfigHost = containers.getServiceHost("cloud-config", CLOUD_CONFIG_PORT);
      var cloudConfigPort = containers.getServicePort("cloud-config", CLOUD_CONFIG_PORT);
      var localDbHost = containers.getServiceHost("local-db", LOCAL_DB_PORT);
      var localDbPort = containers.getServicePort("local-db", LOCAL_DB_PORT);

      properties.put("spring.application.name", "goguma");
      properties.put("spring.profiles.active", "dev");
      properties.put(
          "spring.config.import",
          "optional:configserver:http://" + cloudConfigHost + ":" + cloudConfigPort);
      properties.put("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
      properties.put(
          "spring.datasource.url",
          "jdbc:mysql://" + localDbHost + ":" + localDbPort + "/goguma-bookstore");
      properties.put("spring.datasource.username", "root");
      properties.put("spring.datasource.password", "root1234!");
      properties.put("openapi.data-go-kr.secret-key", "HJGVGr90METjyp2CXImquifQLs4eOdNEkTGf31Da6kqByBCIbkbn8FxO%2BgxrKvAaNzQx7FpjSqNvZCSzBIOY9Q%3D%3D");
      properties.put("openapi.center-lib.secret-key", "6d8a07116c7eab073f5d98230fc96f44fb8285eaa18f2bc8481c3f85ea789a8b");

      TestPropertyValues.of(properties).applyTo(applicationContext);
    }
  }
}
