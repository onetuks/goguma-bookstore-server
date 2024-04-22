package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorDetailsResponse.AuthorDetailsResponses;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class AuthorDetailsResponsesTest extends IntegrationTest {

  @Test
  @DisplayName("작가 프로필 다건 조회 결과 객체를 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    List<AuthorDetailsResult> list =
        IntStream.rangeClosed(1, 3)
            .mapToObj(
                index ->
                    new AuthorDetailsResult(
                        index,
                        "mock-profile.png",
                        "빠니보틀" + index,
                        "유튜브 대통령",
                        "https://www.instagram.com/panibottle" + index,
                        1,
                        2,
                        3))
            .toList();

    Page<AuthorDetailsResult> detailsResults =
        new PageImpl<>(list, PageRequest.of(0, 10), list.size());

    // When
    AuthorDetailsResponses results = AuthorDetailsResponses.from(detailsResults);

    // Then
    assertThat(results.responses())
        .hasSize(3)
        .allSatisfy(
            result -> {
              assertThat(result.authorId()).isPositive();
              assertThat(result.nickname()).contains("빠니보틀");
              assertThat(result.bookCount()).isEqualTo(2);
            });
  }
}
