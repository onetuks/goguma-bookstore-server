package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationCreateResponseTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 신청 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    RegistrationCreateResult postResult =
        new RegistrationCreateResult(
            23L,
            false,
            "검수 중입니다.",
            "https://cover.img.png",
            "유라시아 여행기",
            "대충 베트남에서 시작해서 영국까지",
            20_000L,
            10L,
            "978-89-12345-67-8",
            "포플러",
            true,
            "https://sample.url.pdf");

    // When
    RegistrationCreateResponse result = RegistrationCreateResponse.from(postResult);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(postResult.registrationId()),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo(postResult.approvalMemo()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(postResult.coverImgUrl()),
        () -> assertThat(result.title()).isEqualTo(postResult.title()),
        () -> assertThat(result.summary()).isEqualTo(postResult.summary()),
        () -> assertThat(result.price()).isEqualTo(postResult.price()),
        () -> assertThat(result.stockCount()).isEqualTo(postResult.stockCount()),
        () -> assertThat(result.isbn()).isEqualTo(postResult.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(postResult.publisher()),
        () -> assertThat(result.promotion()).isEqualTo(postResult.promotion()),
        () -> assertThat(result.sampleUrl()).isEqualTo(postResult.sampleUrl()));
  }
}
