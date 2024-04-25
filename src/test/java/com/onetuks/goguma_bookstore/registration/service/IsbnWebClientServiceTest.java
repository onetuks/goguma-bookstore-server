package com.onetuks.goguma_bookstore.registration.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationIsbnResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class IsbnWebClientServiceTest extends IntegrationTest {

  @Autowired private IsbnWebClientService service;

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
    assertThat(result.docs().getFirst().EA_ISBN()).isEqualTo(isbn);
    assertThat(result.docs().getFirst().TITLE()).isNotBlank();
  }
}
