package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationGetResponseTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 조회 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    RegistrationGetResult getResult =
        new RegistrationGetResult(
            1_233L,
            true,
            "approvalMemo",
            "coverImgUrl",
            "title",
            "summary",
            10_000L,
            100L,
            "isbn",
            "publisher",
            true,
            "sampleUrl");

    // When
    RegistrationGetResponse result = RegistrationGetResponse.from(getResult);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(getResult.registrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(getResult.approvalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(getResult.approvalMemo()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(getResult.coverImgUrl()),
        () -> assertThat(result.title()).isEqualTo(getResult.title()),
        () -> assertThat(result.summary()).isEqualTo(getResult.summary()),
        () -> assertThat(result.price()).isEqualTo(getResult.price()),
        () -> assertThat(result.stockCount()).isEqualTo(getResult.stockCount()),
        () -> assertThat(result.isbn()).isEqualTo(getResult.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(getResult.publisher()),
        () -> assertThat(result.promotion()).isEqualTo(getResult.promotion()),
        () -> assertThat(result.sampleUrl()).isEqualTo(getResult.sampleUrl()));
  }
}
