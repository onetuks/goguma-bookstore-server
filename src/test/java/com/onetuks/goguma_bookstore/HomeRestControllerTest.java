package com.onetuks.goguma_bookstore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class HomeRestControllerTest extends IntegrationTest {

  @Autowired private HomeRestController homeRestController;

  @Test
  void home() {
    // Given & When
    String result = homeRestController.home();

    // Then
    assertThat(result).isEqualTo("home");
  }
}
