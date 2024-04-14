package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationInspectionParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationInspectionRequestTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 검수 요청 객체에서 파람 객체로 변환한다.")
  void toTest() {
    // Given
    RegistrationInspectionRequest request = new RegistrationInspectionRequest(true, "검수 완료되었습니다.");

    // When
    RegistrationInspectionParam result = request.to();

    // Then
    assertAll(
        () -> assertThat(result.approvalResult()).isEqualTo(request.approvalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(request.approvalMemo()));
  }
}
