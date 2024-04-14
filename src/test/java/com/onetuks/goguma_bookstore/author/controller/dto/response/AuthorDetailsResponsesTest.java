package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorDetailsResponse.AuthorDetailsResponses;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorDetailsResponsesTest extends IntegrationTest {

  @Test
  @DisplayName("작가 프로필 다건 조회 결과 객체를 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    List<AuthorDetailsResult> detailsResults =
        List.of(
            new AuthorDetailsResult(1_000L, "mock-profile.png", "빠니보틀", "유튜브 대통령", 1, 2, 3),
            new AuthorDetailsResult(1_001L, "mock-profile2.png", "빠니보틀2", "유튜브 대통령2", 1, 2, 3),
            new AuthorDetailsResult(1_002L, "mock-profile3.png", "빠니보틀3", "유튜브 대통령3", 1, 2, 3));

    // When
    AuthorDetailsResponses results = AuthorDetailsResponses.from(detailsResults);

    // Then
    assertThat(results.responses())
        .hasSize(3)
        .allSatisfy(
            result -> {
              assertThat(result.authorId())
                  .isGreaterThanOrEqualTo(1_000L)
                  .isLessThanOrEqualTo(1_002L);
              assertThat(result.nickname()).contains("빠니보틀");
              assertThat(result.bookCount()).isEqualTo(2);
            });
  }
}
