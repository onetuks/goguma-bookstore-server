package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static com.onetuks.goguma_bookstore.global.vo.file.FileType.MAIL_ORDER_SALES;
import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorMailOrderSalesSubmitResult;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.global.vo.file.MailOrderSalesFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorMailOrderSalesFileSubmitResponseTest extends IntegrationTest {

  @Test
  @DisplayName("통신판매신고증 전송 결과를 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    long authorId = 1_000L;
    MailOrderSalesFile mailOrderSalesFile =
        CustomFileFixture.create(authorId, MAIL_ORDER_SALES).toMailOrderSalesFile();
    AuthorMailOrderSalesSubmitResult submitResult =
        new AuthorMailOrderSalesSubmitResult(mailOrderSalesFile.getMailOrderSalesUrl());

    // When
    AuthorMailOrderSalesSubmitResponse result =
        AuthorMailOrderSalesSubmitResponse.from(submitResult);

    // Then
    assertThat(result.mailOrderSalesUrl()).contains(mailOrderSalesFile.getMailOrderSalesUrl());
  }
}
