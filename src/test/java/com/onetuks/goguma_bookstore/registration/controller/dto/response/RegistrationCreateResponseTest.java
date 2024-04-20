package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.model.vo.PageSizeInfo;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationCreateResponseTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 신청 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    RegistrationCreateResult postResult =
        new RegistrationCreateResult(
            23L,
            false,
            "검수 중입니다.",
            "유라시아 여행기",
            "대충 베트남에서 시작해서 영국까지",
            "대 빠니께서 무려 400일 간 유라시아 대륙을 횡단하시며 있었던 일들을 친히 적어내리신 이 시대 최고의 책!",
            List.of(Category.CARTOON, Category.NOVEL),
            "출판사A",
            "978-89-12345-67-8",
            new PageSizeInfo(200, 100),
            "양장본",
            300L,
            20_000L,
            10L,
            true,
            "https://cover.img.png",
            List.of("https://mockup.img.png", "https://mockup.img2.png"),
            List.of("https://preview.img.png", "https://preview.img2.png"),
            "https://sample.url.pdf");

    // When
    RegistrationCreateResponse result = RegistrationCreateResponse.from(postResult);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(postResult.registrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(postResult.approvalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(postResult.approvalMemo()),
        () -> assertThat(result.title()).isEqualTo(postResult.title()),
        () -> assertThat(result.oneLiner()).isEqualTo(postResult.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(postResult.summary()),
        () ->
            assertThat(result.categories())
                .containsExactlyInAnyOrderElementsOf(postResult.categories()),
        () -> assertThat(result.publisher()).isEqualTo(postResult.publisher()),
        () -> assertThat(result.isbn()).isEqualTo(postResult.isbn()),
        () -> assertThat(result.pageSizeInfo()).isEqualTo(postResult.pageSizeInfo()),
        () -> assertThat(result.coverType()).isEqualTo(postResult.coverType()),
        () -> assertThat(result.pageCount()).isEqualTo(postResult.pageCount()),
        () -> assertThat(result.price()).isEqualTo(postResult.price()),
        () -> assertThat(result.stockCount()).isEqualTo(postResult.stockCount()),
        () -> assertThat(result.promotion()).isEqualTo(postResult.promotion()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(postResult.coverImgUrl()),
        () ->
            assertThat(result.detailImgUrls())
                .containsExactlyInAnyOrderElementsOf(postResult.detailImgUrls()),
        () ->
            assertThat(result.previewUrls())
                .containsExactlyInAnyOrderElementsOf(postResult.previewUrls()),
        () -> assertThat(result.sampleUrl()).isEqualTo(postResult.sampleUrl()));
  }
}
