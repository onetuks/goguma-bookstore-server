package com.onetuks.modulereader.registration.controller.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.registration.controller.dto.response.RegistrationInspectionResponse;
import com.onetuks.modulereader.registration.service.dto.result.RegistrationInspectionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationInspectionResponseTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 섬수 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    RegistrationInspectionResult inpsectionResult =
        new RegistrationInspectionResult(1L, true, "통과");

    // When
    RegistrationInspectionResponse result = RegistrationInspectionResponse.from(inpsectionResult);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(inpsectionResult.registrationId()),
        () -> assertThat(result.approvalResult()).isTrue(),
        () -> assertThat(result.approvalMemo()).isEqualTo(inpsectionResult.approvalMemo()));
  }
}
