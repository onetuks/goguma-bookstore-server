package com.onetuks.goguma_bookstore.author.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorCreateEnrollmentRequestTest extends IntegrationTest {

  @Test
  @DisplayName("정상적인 값으로 객체 생성 성공한다.")
  void create_Success() {
    // Given
    String nickname = "빠니보틀";
    String introduction = "빡친감자";
    String instagramUrl = "https://www.instagram.com/pannibottle";

    // When
    AuthorCreateEnrollmentRequest result =
        new AuthorCreateEnrollmentRequest(nickname, introduction, instagramUrl);

    // Then
    assertThat(result)
        .hasFieldOrPropertyWithValue("nickname", nickname)
        .hasFieldOrPropertyWithValue("introduction", introduction)
        .hasFieldOrPropertyWithValue("instagramUrl", instagramUrl);
  }

  @Test
  @DisplayName("입점 신청 Param 객체 생성한다.")
  void createParam_Test() {
    // Given
    String nickname = "빠니보틀";
    String introduction = "빡친감자";
    String instagramUrl = "https://www.instagram.com/pannibottle";

    // When
    AuthorCreateParam result =
        new AuthorCreateEnrollmentRequest(nickname, introduction, instagramUrl).to();

    // Then
    assertAll(
        () -> assertThat(result.nickname()).isEqualTo(nickname),
        () -> assertThat(result.introduction()).isEqualTo(introduction),
        () -> assertThat(result.instagramUrl()).isEqualTo(instagramUrl));
  }
}
