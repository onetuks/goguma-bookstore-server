package com.onetuks.goguma_bookstore.author.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class AuthorCreateRequestTest {

  @Test
  @DisplayName("정상적인 값으로 객체 생성 성공한다.")
  void create_Success() {
    // Given
    String nickname = "빠니보틀";
    String introduction = "빡친감자";

    // When
    AuthorCreateRequest result = new AuthorCreateRequest(nickname, introduction);

    // Then
    assertThat(result)
        .hasFieldOrPropertyWithValue("nickname", nickname)
        .hasFieldOrPropertyWithValue("introduction", introduction);
  }

  @Test
  @DisplayName("멀티파트파일을 인자로 받아서 Param 객체 생성한다.")
  void createParam_Test() throws IOException {
    // Given
    Long memberId = 1_000L;
    String nickname = "빠니보틀";
    String introduction = "빡친감자";
    MultipartFile profileImg = MultipartFileFixture.createFile(FileType.PROFILES);

    // When
    AuthorCreateParam result = new AuthorCreateRequest(nickname, introduction).to();

    // Then
    assertThat(result.nickname()).isEqualTo(nickname);
  }
}
