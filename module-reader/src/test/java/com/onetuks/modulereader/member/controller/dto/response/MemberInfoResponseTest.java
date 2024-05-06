package com.onetuks.modulereader.member.controller.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulereader.ReaderIntegrationTest;
import com.onetuks.modulereader.member.service.dto.result.MemberInfoResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberInfoResponseTest extends ReaderIntegrationTest {

  @Test
  @DisplayName("멤버 정보 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberInfoResult infoResult =
        new MemberInfoResult(
            1_000L,
            "빠니보틀",
            "bbanibottle.png",
            true,
            "강원도 춘천시 중앙로",
            "대 빠니보틀 생가",
            CashReceiptType.PERSON,
            "010-0101-0101");

    // When
    MemberInfoResponse result = MemberInfoResponse.from(infoResult);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(infoResult.memberId()),
        () -> assertThat(result.nickname()).isEqualTo(infoResult.nickname()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(infoResult.profileImgUrl()),
        () -> assertThat(result.alarmPermission()).isEqualTo(infoResult.alarmPermission()),
        () -> assertThat(result.defaultAddress()).isEqualTo(infoResult.defaultAddress()),
        () ->
            assertThat(result.defaultAddressDetail()).isEqualTo(infoResult.defaultAddressDetail()),
        () ->
            assertThat(result.defaultCashReceiptType())
                .isEqualTo(infoResult.defaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(infoResult.defaultCashReceiptNumber()));
  }
}
