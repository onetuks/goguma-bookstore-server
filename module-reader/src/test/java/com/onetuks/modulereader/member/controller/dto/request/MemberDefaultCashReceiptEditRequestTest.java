package com.onetuks.modulereader.member.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.member.service.dto.param.MemberDefaultCashReceiptEditParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberDefaultCashReceiptEditRequestTest extends IntegrationTest {

  @Test
  @DisplayName("기본 현금영수증 수정 요청 객체에서 파람 객체로 변환한다.")
  void toTest() {
    // Given
    MemberDefaultCashReceiptEditRequest request =
        new MemberDefaultCashReceiptEditRequest(CashReceiptType.COMPANY.name(), "010-0101-0101");

    // When
    MemberDefaultCashReceiptEditParam result = request.to();

    // Then
    assertAll(
        () ->
            assertThat(result.defaultCashReceiptType().name())
                .isEqualTo(request.defaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(request.defaultCashReceiptNumber()));
  }
}
