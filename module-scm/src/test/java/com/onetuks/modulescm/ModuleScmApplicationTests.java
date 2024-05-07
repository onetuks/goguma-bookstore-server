package com.onetuks.modulescm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ModuleScmApplicationTests extends ScmIntegrationTest {

  @Test
  void contextLoads() {
    assertThat(getClass().getSimpleName()).isEqualTo("ModuleScmApplicationTests");
  }
}
