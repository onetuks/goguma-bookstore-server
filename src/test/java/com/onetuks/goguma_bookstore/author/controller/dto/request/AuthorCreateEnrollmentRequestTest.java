package com.onetuks.goguma_bookstore.author.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorCreateEnrollmentRequestTest {

  @Test
  @DisplayName("정상적인 값으로 객체 생성 성공한다.")
  void create_Success() {
    // Given
    String nickname = "빠니보틀";
    String introduction = "빡친감자";

    // When
    AuthorCreateEnrollmentRequest result =
        new AuthorCreateEnrollmentRequest(nickname, introduction);

    // Then
    assertThat(result)
        .hasFieldOrPropertyWithValue("nickname", nickname)
        .hasFieldOrPropertyWithValue("introduction", introduction);
  }

  @Test
  @DisplayName("입점 신청 Param 객체 생성한다.")
  void createParam_Test() {
    // Given
    long memberId = 1_000L;
    String nickname = "빠니보틀";
    String introduction = "빡친감자";

    // When
    AuthorCreateParam result = new AuthorCreateEnrollmentRequest(nickname, introduction).to();

    // Then
    assertThat(result.nickname()).isEqualTo(nickname);
  }
}
