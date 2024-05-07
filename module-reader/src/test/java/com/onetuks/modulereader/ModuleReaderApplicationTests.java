package com.onetuks.modulereader;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ModuleReaderApplicationTests extends ReaderIntegrationTest {

  @Test
  void contextLoads() {
    assertThat(getClass().getSimpleName()).isEqualTo("ModuleReaderApplicationTests");
  }
}
