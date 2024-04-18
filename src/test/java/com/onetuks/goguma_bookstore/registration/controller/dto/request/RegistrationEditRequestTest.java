package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationEditParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationEditRequestTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 수정 요청 객체를 파람 객체로 변환한다.")
  void toTest() {
    // Given
    RegistrationEditRequest request =
        new RegistrationEditRequest("신간 제목", "신간 요약", 10000L, 100L, "1234567890123", "출판사", true);

    // When
    RegistrationEditParam result = request.to();

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
