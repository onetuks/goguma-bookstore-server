package com.onetuks.goguma_bookstore;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ControllerTest extends IntegrationTest {

  @Autowired private TestController testController;

  @Test
  void homeTest() {
    // Given & When
    String result = testController.home();

    // Then
    assertThat(result).isEqualTo("home");
  }
}
