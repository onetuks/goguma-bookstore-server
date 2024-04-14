package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationEditResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationEditResponseTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 수정 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    RegistrationEditResult editResult =
        new RegistrationEditResult(
            1L,
            true,
            "승인",
            "coverImgUrl",
            "title",
            "summary",
            10000L,
            100L,
            "isbn",
            "publisher",
            true,
            "sampleUrl");

    // When
    RegistrationEditResponse result = RegistrationEditResponse.from(editResult);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(editResult.registrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(editResult.approvalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(editResult.approvalMemo()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(editResult.coverImgUrl()),
        () -> assertThat(result.title()).isEqualTo(editResult.title()),
        () -> assertThat(result.summary()).isEqualTo(editResult.summary()),
        () -> assertThat(result.price()).isEqualTo(editResult.price()),
        () -> assertThat(result.stockCount()).isEqualTo(editResult.stockCount()),
        () -> assertThat(result.isbn()).isEqualTo(editResult.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(editResult.publisher()),
        () -> assertThat(result.promotion()).isEqualTo(editResult.promotion()),
        () -> assertThat(result.sampleUrl()).isEqualTo(editResult.sampleUrl()));
  }
}
