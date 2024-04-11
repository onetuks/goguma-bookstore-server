package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentDetailsResponse.AuthorEnrollmentDetailsResponses;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorEnrollmentDetailsResponsesTest extends IntegrationTest {

  @Test
  @DisplayName("입점 심사 상세 다건 조회 결과 객체를 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    List<AuthorEnrollmentDetailsResult> results =
        Stream.of(
                AuthorFixture.createDetailsResult(),
                AuthorFixture.createDetailsResult(),
                AuthorFixture.createDetailsResult(),
                AuthorFixture.createDetailsResult(),
                AuthorFixture.createDetailsResult(),
                AuthorFixture.createDetailsResult(),
                AuthorFixture.createDetailsResult())
            .filter(result -> result.roleType() == RoleType.USER)
            .toList();

    // When
    AuthorEnrollmentDetailsResponses responses = AuthorEnrollmentDetailsResponses.from(results);

    // Then
    assertThat(responses.responses())
        .hasSize(results.size())
        .allSatisfy(
            response -> {
              assertThat(response.roleType()).isEqualTo(RoleType.USER);
              assertThat(response.enrollmentPassed()).isFalse();
              assertThat(response.profileImgUrl()).contains(String.valueOf(response.authorId()));
            });
  }
}
