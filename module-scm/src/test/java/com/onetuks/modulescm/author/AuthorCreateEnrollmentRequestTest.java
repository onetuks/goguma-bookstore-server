package com.onetuks.modulescm.author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulescm.IntegrationTest;
import com.onetuks.modulescm.author.controller.dto.request.AuthorCreateEnrollmentRequest;
import com.onetuks.modulescm.author.service.dto.param.AuthorCreateEnrollmentParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorCreateEnrollmentRequestTest extends IntegrationTest {

  @Test
  @DisplayName("정상적인 값으로 객체 생성 성공한다.")
  void create_Success() {
    // Given
    String nickname = "빠니보틀";
    String introduction = "빡친감자";
    String instagramUrl = "https://www.instagram.com/pannibottle";
    String businessNumber = "1234567890";
    String mailOrderSalesNumber = "1234567890";

    // When
    AuthorCreateEnrollmentRequest result =
        new AuthorCreateEnrollmentRequest(
            nickname, introduction, instagramUrl, businessNumber, mailOrderSalesNumber);

    // Then
    assertThat(result)
        .hasFieldOrPropertyWithValue("nickname", nickname)
        .hasFieldOrPropertyWithValue("introduction", introduction)
        .hasFieldOrPropertyWithValue("instagramUrl", instagramUrl)
        .hasFieldOrPropertyWithValue("businessNumber", businessNumber)
        .hasFieldOrPropertyWithValue("mailOrderSalesNumber", mailOrderSalesNumber);
  }

  @Test
  @DisplayName("입점 신청 Param 객체 생성한다.")
  void createParam_Test() {
    // Given
    String nickname = "빠니보틀";
    String introduction = "빡친감자";
    String instagramUrl = "https://www.instagram.com/pannibottle";
    String businessNumber = "1234567890";
    String mailOrderSalesNumber = "1234567890";

    // When
    AuthorCreateEnrollmentParam result =
        new AuthorCreateEnrollmentRequest(
                nickname, introduction, instagramUrl, businessNumber, mailOrderSalesNumber)
            .to();

    // Then
    assertAll(
        () -> assertThat(result.nickname()).isEqualTo(nickname),
        () -> assertThat(result.introduction()).isEqualTo(introduction),
        () -> assertThat(result.instagramUrl()).isEqualTo(instagramUrl),
        () -> assertThat(result.businessNumber()).isEqualTo(businessNumber),
        () -> assertThat(result.mailOrderSalesNumber()).isEqualTo(mailOrderSalesNumber));
  }
}
