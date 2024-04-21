package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorEditResponseTest extends IntegrationTest {

  @Test
  @DisplayName("작가 프로필 정보 수정 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    AuthorEditResult editResult =
        new AuthorEditResult(
            1_000L, "profileImgUrl.png", "빠니보틀", "유튜브 대통령", "https://instagram.com/panibottle");

    // When
    AuthorEditResponse result = AuthorEditResponse.from(editResult);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(editResult.authorId()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(editResult.profileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(editResult.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(editResult.introduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(editResult.instagramUrl()));
  }
}
