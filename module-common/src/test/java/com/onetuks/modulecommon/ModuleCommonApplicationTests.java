package com.onetuks.modulecommon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ModuleCommonApplicationTests extends CommonIntegrationTest {

  @Test
  void contextLoads() {
    assertThat(getClass().getSimpleName()).isEqualTo("ModuleCommonApplicationTests");
  }
}
