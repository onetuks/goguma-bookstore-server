package com.onetuks.goguma_bookstore.member.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberProfileEditResult;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberProfileEditResponseTest extends IntegrationTest {

  @Test
  @DisplayName("멤버 프로필 수정 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberProfileEditResult editResult =
        new MemberProfileEditResult(
            1_000L,
            "빠니보틀",
            "https://goguma-bookstore.s3.ap-northeast-2.amazonaws.com/profile/1_000.jpg",
            true,
            "서울특별시 강남구 역삼동 123-456",
            "아파트 123동 456호",
            CashReceiptType.PERSON,
            "010-1234-5678");

    // When
    MemberProfileEditResponse result = MemberProfileEditResponse.from(editResult);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(editResult.memberId()),
        () -> assertThat(result.nickname()).isEqualTo(editResult.nickname()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(editResult.profileImgUrl()),
        () -> assertThat(result.alarmPermission()).isEqualTo(editResult.alarmPermission()),
        () -> assertThat(result.defaultAddress()).isEqualTo(editResult.defaultAddress()),
        () ->
            assertThat(result.defaultAddressDetail()).isEqualTo(editResult.defaultAddressDetail()),
        () ->
            assertThat(result.defaultCashReceiptType())
                .isEqualTo(editResult.defaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(editResult.defaultCashReceiptNumber()));
  }
}