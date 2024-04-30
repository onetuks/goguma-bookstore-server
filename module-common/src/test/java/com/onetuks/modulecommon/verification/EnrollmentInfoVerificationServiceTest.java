package com.onetuks.modulecommon.verification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.modulecommon.CommonIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EnrollmentInfoVerificationServiceTest extends CommonIntegrationTest {

  @Autowired private EnrollmentInfoVerificationService service;

  @Test
  @DisplayName("정상적인 사업자등록번호와 통신판매신고번호를 입력했을 때, 정상적으로 검증이 완료된다.")
  void verifyEnrollmentInfoTest() {
    // Given
    String businessNumber = "2062753344";
    String mailOrderSalesNumber = "876593954069799775";

    // When & Then
    assertDoesNotThrow(() -> service.verifyEnrollmentInfo(businessNumber, mailOrderSalesNumber));
  }

  @Test
  @DisplayName("존재하지 않는 사업자등록번호를 입력했을 때, IllegalArgumentException 이 발생한다.")
  void verifyEnrollmentInfoTestWithInvalidBusinessNumber() {
    // Given
    String businessNumber = "1234567890";
    String mailOrderSalesNumber = "875645983869799600";

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> service.verifyEnrollmentInfo(businessNumber, mailOrderSalesNumber));
  }

  @Test
  @DisplayName("존재하지 않는 통신판매신고번호를 입력했을 때, IllegalArgumentException이 발생한다.")
  void verifyEnrollmentInfoTestWithInvalidMailOrderSalesNumber() {
    // Given
    String businessNumber = "6615900628";
    String mailOrderSalesNumber = "1";

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> service.verifyEnrollmentInfo(businessNumber, mailOrderSalesNumber));
  }
}
