package com.onetuks.modulereader.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.author.service.dto.result.AuthorDetailsResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorDetailsResponseTest extends IntegrationTest {

  @Test
  @DisplayName("작가 프로필 조회 결과 객체를 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    AuthorDetailsResult expected =
        new AuthorDetailsResult(
            1_000L,
            "mock-profile.png",
            "빠니보틀",
            "유튜브 대통령",
            "https://www.instagram.com/panibottle",
            101,
            102,
            103);

    // When
    AuthorDetailsResponse result = AuthorDetailsResponse.from(expected);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(expected.authorId()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(expected.profileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(expected.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(expected.introduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(expected.instagramUrl()),
        () -> assertThat(result.subscribeCount()).isEqualTo(101),
        () -> assertThat(result.bookCount()).isEqualTo(102),
        () -> assertThat(result.restockCount()).isEqualTo(103));
  }
}
