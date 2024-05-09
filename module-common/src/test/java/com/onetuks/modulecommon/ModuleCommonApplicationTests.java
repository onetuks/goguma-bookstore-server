package com.onetuks.modulecommon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

class ModuleCommonApplicationTests extends CommonIntegrationTest {

  private final Logger log = LoggerFactory.getLogger(ModuleCommonApplicationTests.class);

  @Autowired private Environment env;

  @Test
  void contextLoads() {
    log.info("=== Start of properties logging ===");
    for (String key : env.getActiveProfiles()) {
      String value = env.getProperty(key);
      log.info("Property: {} = {}", key, value);
    }
    log.info("=== End of properties logging ===");

    assertThat(getClass().getSimpleName()).isEqualTo("ModuleCommonApplicationTests");
  }
}
