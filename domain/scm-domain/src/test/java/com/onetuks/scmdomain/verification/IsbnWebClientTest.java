package com.onetuks.scmdomain.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.filestorage.CommonIntegrationTest;
import com.onetuks.filestorage.verification.webclient.IsbnWebClient;
import com.onetuks.filestorage.verification.webclient.dto.result.RegistrationIsbnResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class IsbnWebClientTest extends CommonIntegrationTest {

  @Autowired private IsbnWebClient service;

  @Test
  @DisplayName("존재하는 ISBN로 요청 시 책 정보를 응답 받는다.")
  void requestDataTest() {
    // Given
    String isbn = "9788999278488";

    // When
    RegistrationIsbnResult result = service.requestData(isbn);

    // Then
    assertThat(result).isNotNull();
    assertAll(
        () -> assertThat(Long.parseLong(result.TOTAL_COUNT())).isPositive(),
        () -> assertThat(result.docs()).isNotEmpty());
    assertThat(result.docs().get(0).EA_ISBN()).isEqualTo(isbn);
    assertThat(result.docs().get(0).TITLE()).isNotBlank();
  }
}
