package com.onetuks.modulereader.author.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulereader.ReaderIntegrationTest;
import com.onetuks.modulereader.author.service.dto.param.AuthorEditParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorEntityEditRequestTest extends ReaderIntegrationTest {

  @Test
  @DisplayName("작가 프로필 요청 객체에서 매개 객체로 변환한다.")
  void toTest() {
    // Given
    AuthorEditRequest request =
        new AuthorEditRequest("빠니보틀", "유튜브 대통령", "https://www.instagram.com/bbani_bottle");

    // When
    AuthorEditParam result = request.to();

    // Then
    assertAll(
        () -> assertThat(result.nickname()).isEqualTo(request.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(request.introduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(request.instagramUrl()));
  }
}
