package com.onetuks.scmdomain.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.scmdomain.ScmDomainIntegrationTest;
import com.onetuks.scmdomain.verification.webclient.BusinessNumberWebClient;
import com.onetuks.scmdomain.verification.webclient.dto.response.BusinessNumberResponse;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BusinessNumberWebClientTest extends ScmDomainIntegrationTest {

  @Autowired private BusinessNumberWebClient businessNumberWebClient;

  @Test
  @DisplayName("유효한 사업자번호를 이용해서 정상 상태를 조회한다.")
  void requestDataTest() {
    // Given
    String businessNumber = "8188700775";

    // When
    BusinessNumberResponse result = businessNumberWebClient.requestData(businessNumber);

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
