package com.onetuks.goguma_bookstore.author.controller.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorEditParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorEditRequestTest extends IntegrationTest {

  @Test
  @DisplayName("작가 프로필 요청 객체에서 매개 객체로 변환한다.")
  void toTest() {
    // Given
    AuthorEditRequest request = new AuthorEditRequest("빠니보틀", "유튜브 대통령");

    // When
    AuthorEditParam result = request.to();

    // Then
    assertAll(
        () -> assertEquals("빠니보틀", result.nickname()),
        () -> assertEquals("유튜브 대통령", result.introduction()));
  }
}
