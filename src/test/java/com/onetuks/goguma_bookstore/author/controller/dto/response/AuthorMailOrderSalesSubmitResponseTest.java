package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorMailOrderSalesSubmitResult;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class AuthorMailOrderSalesSubmitResponseTest extends IntegrationTest {

  @Test
  @DisplayName("통신판매신고증 전송 결과를 응답 객체로 변환한다.")
  void fromTest() throws IOException {
    // Given
    long authorId = 1_000L;
    MultipartFile mailOrderSalesFile =
        MultipartFileFixture.createFile(FileType.MAIL_ORDER_SALES, authorId);
    AuthorMailOrderSalesSubmitResult submitResult =
        new AuthorMailOrderSalesSubmitResult(mailOrderSalesFile.getName());

    // When
    AuthorMailOrderSalesSubmitResponse result =
        AuthorMailOrderSalesSubmitResponse.from(submitResult);

    // Then
    assertThat(result.mailOrderSalesUrl()).contains(mailOrderSalesFile.getName());
  }
}
