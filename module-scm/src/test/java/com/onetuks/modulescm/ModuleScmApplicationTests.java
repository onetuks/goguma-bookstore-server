package com.onetuks.modulescm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ModuleScmApplicationTests {

  @Test
  void contextLoads() {
    assertThat(getClass().getSimpleName()).isEqualTo("ModuleScmApplicationTests");
  }
}
