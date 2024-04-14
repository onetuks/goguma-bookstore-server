package com.onetuks.goguma_bookstore.member.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberDefaultCashReceiptEditResult;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberDefaultCashReceiptEditResponseTest extends IntegrationTest {

  @Test
  @DisplayName("멤버 기본 현금영수증 수정 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberDefaultCashReceiptEditResult editResult =
        new MemberDefaultCashReceiptEditResult(CashReceiptType.PERSON, "010-0101-0101");

    // When
    MemberDefaultCashReceiptEditResponse result =
        MemberDefaultCashReceiptEditResponse.from(editResult);

    // Then
    assertAll(
        () ->
            assertThat(result.defaultCashReceiptType())
                .isEqualTo(editResult.defaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(editResult.defaultCashReciptNumber()));
  }
}
