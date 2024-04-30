package com.onetuks.modulereader.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.author.service.dto.result.AuthorCreateEnrollmentResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorCreateEnrollmentResponseTest extends IntegrationTest {

  @Test
  @DisplayName("생성 결과 객체에서 생성 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    long authorId = 1_000L;
    FileWrapper profileImgFile = FileWrapperFixture.createFile(authorId, FileType.PROFILES);
    AuthorCreateEnrollmentResult resultObject =
        new AuthorCreateEnrollmentResult(
            authorId,
            profileImgFile.getUri(),
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
