package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEnrollmentDetailsResponse.AuthorEnrollmentDetailsResponses;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class AuthorEnrollmentDetailsResponsesTest extends IntegrationTest {

  @Test
  @DisplayName("입점 심사 상세 다건 조회 결과 객체를 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    List<AuthorEnrollmentDetailsResult> list =
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

    Page<AuthorEnrollmentDetailsResult> results =
        new PageImpl<>(list, PageRequest.of(0, 10), list.size());

    // When
    AuthorEnrollmentDetailsResponses responses = AuthorEnrollmentDetailsResponses.from(results);

    // Then
    assertThat(responses.responses())
        .hasSize(list.size())
        .allSatisfy(
            response -> {
              assertThat(response.roleType()).isEqualTo(RoleType.USER);
              assertThat(response.enrollmentPassed()).isFalse();
              assertThat(response.profileImgUrl()).contains(String.valueOf(response.authorId()));
            });
  }
}
