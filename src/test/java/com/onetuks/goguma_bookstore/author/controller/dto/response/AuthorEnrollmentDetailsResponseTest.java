package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorEnrollmentDetailsResponseTest extends IntegrationTest {

  @Test
  @DisplayName("작가 입점 정보 요청 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    AuthorEnrollmentDetailsResult detailsResult = AuthorFixture.createDetailsResult();

    // When
    AuthorEnrollmentDetailsResponse result = AuthorEnrollmentDetailsResponse.from(detailsResult);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(detailsResult.authorId()),
        () -> assertThat(result.memberId()).isEqualTo(detailsResult.memberId()),
        () -> assertThat(result.escrowServiceUrl()).isEqualTo(detailsResult.escrowServiceUrl()),
        () -> assertThat(result.enrollmentPassed()).isEqualTo(detailsResult.enrollmentPassed()));
  }
}
