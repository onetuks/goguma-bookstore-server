package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.book.vo.Category;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationResponseTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 조회 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    RegistrationResult getResult =
        new RegistrationResult(
            1_233L,
            true,
            "심사 통과했습니다.",
            "제목",
            "한줄소개",
            "줄거리 요약",
            List.of(Category.NOVEL, Category.ESSEY),
            "isbn",
            200,
            100,
            "책 표지 타입",
            300L,
            20_000L,
            10_000L,
            true,
            "출판사",
            100L,
            "표지 이미지 URL",
            List.of("상세 이미지 URL1", "상세 이미지 URL2"),
            List.of("미리보기 URL1", "미리보기 URL2"),
            "샘플 URL");

    // When
    RegistrationResponse result = RegistrationResponse.from(getResult);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(getResult.registrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(getResult.approvalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(getResult.approvalMemo()),
        () -> assertThat(result.title()).isEqualTo(getResult.title()),
        () -> assertThat(result.oneLiner()).isEqualTo(getResult.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(getResult.summary()),
        () -> assertThat(result.categories()).isEqualTo(getResult.categories()),
        () -> assertThat(result.isbn()).isEqualTo(getResult.isbn()),
        () -> assertThat(result.height()).isEqualTo(getResult.height()),
        () -> assertThat(result.width()).isEqualTo(getResult.width()),
        () -> assertThat(result.coverType()).isEqualTo(getResult.coverType()),
        () -> assertThat(result.pageCount()).isEqualTo(getResult.pageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(getResult.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(getResult.purchasePrice()),
        () -> assertThat(result.promotion()).isEqualTo(getResult.promotion()),
        () -> assertThat(result.publisher()).isEqualTo(getResult.publisher()),
        () -> assertThat(result.stockCount()).isEqualTo(getResult.stockCount()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(getResult.coverImgUrl()),
        () -> assertThat(result.detailImgUrls()).isEqualTo(getResult.detailImgUrls()),
        () -> assertThat(result.previewUrls()).isEqualTo(getResult.previewUrls()),
        () -> assertThat(result.sampleUrl()).isEqualTo(getResult.sampleUrl()));
  }
}
