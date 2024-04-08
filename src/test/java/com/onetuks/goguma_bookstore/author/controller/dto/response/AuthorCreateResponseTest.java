package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateResult;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorCreateResponseTest {

  @Test
  @DisplayName("생성 결과 객체에서 생성 응답 객체로 변환한다.")
  void from() {
    // Given
    AuthorCreateResult resultObject =
        new AuthorCreateResult(1_000L, MockMultipartFileInfo.PROFILE.getFileName(), "빠니보틀", "빡친감자");

    // When
    AuthorCreateResponse result = AuthorCreateResponse.from(resultObject);

    // Then
    assertThat(result.authorId()).isEqualTo(resultObject.authorId());
  }
}
