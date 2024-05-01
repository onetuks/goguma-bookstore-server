package com.onetuks.moduleauth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class ModuleAuthApplicationTests extends AuthIntegrationTest {

  @Test
  void contextLoads() {
    assertThat(getClass().getSimpleName()).isEqualTo("ModuleAuthApplicationTests");
  }
}
