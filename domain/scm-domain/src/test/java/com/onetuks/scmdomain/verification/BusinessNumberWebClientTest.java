package com.onetuks.scmdomain.verification;

import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.filestorage.CommonIntegrationTest;
import com.onetuks.filestorage.verification.webclient.BusinessNumberWebClient;
import com.onetuks.filestorage.verification.webclient.dto.response.BusinessNumberResponse;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BusinessNumberWebClientTest extends CommonIntegrationTest {

  @Autowired private BusinessNumberWebClient service;

  @Test
  @DisplayName("유효한 사업자번호를 이용해서 정상 상태를 조회한다.")
  void requestDataTest() {
    // Given
    String businessNumber = "8188700775";

    // When
    BusinessNumberResponse result = service.requestData(businessNumber);

    // Then
    assertAll(
        () -> assertThat(result.status_code()).isEqualTo("OK"),
        () -> assertThat(result.match_cnt()).isPositive(),
        () -> assertThat(result.data()).isNotEmpty(),
        () ->
            assertThat(Objects.requireNonNull(result.data()).get(0).b_no())
                .isEqualTo(businessNumber));
  }
}
