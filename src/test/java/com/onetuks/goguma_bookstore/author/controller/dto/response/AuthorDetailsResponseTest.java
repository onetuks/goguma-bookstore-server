package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorDetailsResponseTest extends IntegrationTest {

  @Test
  @DisplayName("작가 프로필 조회 결과 객체를 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    AuthorDetailsResult expected =
        new AuthorDetailsResult(1_000L, "mock-profile.png", "빠니보틀", "유튜브 대통령");

    // When
    AuthorDetailsResponse result = AuthorDetailsResponse.from(expected);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(expected.authorId()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(expected.profileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(expected.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(expected.introduction()));
  }
}
