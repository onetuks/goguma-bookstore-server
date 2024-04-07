package com.onetuks.goguma_bookstore.author_debut.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.author_debut.service.dto.result.AuthorDebutCreateResult;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture.MockMultipartFileInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorDebutCreateResponseTest {

  @Test
  @DisplayName("생성 결과 객체에서 생성 응답 객체로 변환한다.")
  void from() {
    // Given
    AuthorDebutCreateResult resultObject =
        new AuthorDebutCreateResult(
            1_000L, MockMultipartFileInfo.PROFILE.getFileName(), "빠니보틀", "빡친감자");

    // When
    AuthorDebutCreateResponse result = AuthorDebutCreateResponse.from(resultObject);

    // Then
    assertThat(result.memberId()).isEqualTo(resultObject.memberId());
  }
}
