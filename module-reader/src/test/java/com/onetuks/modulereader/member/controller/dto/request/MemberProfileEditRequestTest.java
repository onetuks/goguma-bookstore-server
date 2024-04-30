package com.onetuks.modulereader.member.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.member.service.dto.param.MemberProfileEditParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberProfileEditRequestTest extends IntegrationTest {

  @Test
  @DisplayName("멤버 프로필 수정 요청 객체에서 파람 객체로 변환한다.")
  void toTest() {
    // Given
    MemberProfileEditRequest request =
        new MemberProfileEditRequest(
            "빠니보틀",
            true,
            "defaultAddress",
            "defaultAddressDetail",
            CashReceiptType.COMPANY.name(),
            "defaultCashReceiptNumber");

    // When
    MemberProfileEditParam result = request.to();

    // Then
    assertAll(
        () -> assertThat(result.nickname()).isEqualTo(request.nickname()),
        () -> assertThat(result.alarmPermission()).isEqualTo(request.alarmPermission()),
        () -> assertThat(result.defaultAddress()).isEqualTo(request.defaultAddress()),
        () -> assertThat(result.defaultAddressDetail()).isEqualTo(request.defaultAddressDetail()),
        () ->
            assertThat(result.defaultCashReceiptType().name())
                .isEqualTo(request.defaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(request.defaultCashReceiptNumber()));
  }
}
