package com.onetuks.goguma_bookstore.author.service.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.verification.dto.response.BusinessNumberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BusinessNumberWebClientServiceTest extends IntegrationTest {

  @Autowired private BusinessNumberWebClientService service;

  @Test
  @DisplayName("유효한 사업자번호를 이용해서 정상 상태를 조회한다.")
  void requestDataTest() {
    // Given
    String businessNumber = "8073401609";

    // When
    BusinessNumberResponse result = service.requestData(businessNumber);

    // Then
    assertAll(
        () -> assertThat(result.status_code()).isEqualTo("OK"),
        () -> assertThat(result.match_cnt()).isPositive(),
        () -> assertThat(result.data()).isNotEmpty(),
        () -> assertThat(result.data().get(0).b_no()).isEqualTo(businessNumber));
  }
}
