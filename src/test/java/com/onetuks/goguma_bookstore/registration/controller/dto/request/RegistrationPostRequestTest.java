package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationPostParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationPostRequestTest extends IntegrationTest {

  @Test
  @DisplayName("신간 등록 요청 객체에서 파람 객체로 변환한다.")
  void toTest() {
    // Given
    RegistrationPostRequest request =
        new RegistrationPostRequest(
            "유라시아 여행기", "대충 베트남에서 시작해서 영국까지", 20_000L, 10L, "978-89-12345-67-8", "포플러", true);

    // When
    RegistrationPostParam result = request.to();

    // Then
    assertAll(
        () -> assertThat(result.title()).isEqualTo(request.title()),
        () -> assertThat(result.summary()).isEqualTo(request.summary()),
        () -> assertThat(result.price()).isEqualTo(request.price()),
        () -> assertThat(result.stockCount()).isEqualTo(request.stockCount()),
        () -> assertThat(result.isbn()).isEqualTo(request.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(request.publisher()),
        () -> assertThat(result.promotion()).isEqualTo(request.promotion()));
  }
}
