package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorCreateEnrollmentResponseTest extends IntegrationTest {

  @Test
  @DisplayName("생성 결과 객체에서 생성 응답 객체로 변환한다.")
  void from() throws IOException {
    // Given
    long authorId = 1_000L;
    String fileName = MultipartFileFixture.createFile(FileType.PROFILES, authorId).getName();
    AuthorCreateEnrollmentResult resultObject =
        new AuthorCreateEnrollmentResult(authorId, fileName, "빠니보틀", "빡친감자");

    // When
    AuthorCreateEnrollmentResponse result = AuthorCreateEnrollmentResponse.from(resultObject);

    // Then
    assertThat(result.authorId()).isEqualTo(resultObject.authorId());

    MultipartFileFixture.deleteFile(fileName);
  }
}
