package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.global.vo.file.ProfileImgFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorCreateEnrollmentResponseTest extends IntegrationTest {

  @Test
  @DisplayName("생성 결과 객체에서 생성 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    long authorId = 1_000L;
    ProfileImgFile profileImgFile =
        CustomFileFixture.createFile(authorId, FileType.PROFILES).toProfileImgFile();
    AuthorCreateEnrollmentResult resultObject =
        new AuthorCreateEnrollmentResult(
            authorId,
            profileImgFile.getProfileImgUrl(),
            "빠니보틀",
            "빡친감자",
            "https://www.instagram.com/pannibottle",
            "1234567890",
            "1234567890");

    // When
    AuthorCreateEnrollmentResponse result = AuthorCreateEnrollmentResponse.from(resultObject);

    // Then
    assertThat(result.authorId()).isEqualTo(resultObject.authorId());
  }
}
