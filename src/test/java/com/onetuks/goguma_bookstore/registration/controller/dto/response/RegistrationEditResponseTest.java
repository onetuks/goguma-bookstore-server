package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationEditResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationEditResponseTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 수정 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    RegistrationEditResult editResult =
        new RegistrationEditResult(
            23L,
            false,
            "검수 중입니다.",
            "유라시아 여행기",
            "대충 베트남에서 시작해서 영국까지",
            "대 빠니께서 무려 400일 간 유라시아 대륙을 횡단하시며 있었던 일들을 친히 적어내리신 이 시대 최고의 책!",
            List.of(Category.CARTOON, Category.NOVEL),
            "978-89-12345-67-8",
            200,
            100,
            "양장본",
            300L,
            20_000L,
            10_000L,
            true,
            "출판사A",
            10L,
            "https://cover.img.png",
            List.of("https://mockup.img.png", "https://mockup.img2.png"),
            List.of("https://preview.img.png", "https://preview.img2.png"),
            "https://sample.url.pdf");

    // When
    RegistrationEditResponse result = RegistrationEditResponse.from(editResult);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(editResult.registrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(editResult.approvalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(editResult.approvalMemo()),
        () -> assertThat(result.title()).isEqualTo(editResult.title()),
        () -> assertThat(result.oneLiner()).isEqualTo(editResult.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editResult.summary()),
        () -> assertThat(result.categories()).isEqualTo(editResult.categories()),
        () -> assertThat(result.isbn()).isEqualTo(editResult.isbn()),
        () -> assertThat(result.height()).isEqualTo(editResult.height()),
        () -> assertThat(result.width()).isEqualTo(editResult.width()),
        () -> assertThat(result.coverType()).isEqualTo(editResult.coverType()),
        () -> assertThat(result.pageCount()).isEqualTo(editResult.pageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(editResult.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(editResult.purchasePrice()),
        () -> assertThat(result.promotion()).isEqualTo(editResult.promotion()),
        () -> assertThat(result.publisher()).isEqualTo(editResult.publisher()),
        () -> assertThat(result.stockCount()).isEqualTo(editResult.stockCount()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(editResult.coverImgUrl()),
        () -> assertThat(result.detailImgUrls()).isEqualTo(editResult.detailImgUrls()),
        () -> assertThat(result.previewUrls()).isEqualTo(editResult.previewUrls()),
        () -> assertThat(result.sampleUrl()).isEqualTo(editResult.sampleUrl()));
  }
}
