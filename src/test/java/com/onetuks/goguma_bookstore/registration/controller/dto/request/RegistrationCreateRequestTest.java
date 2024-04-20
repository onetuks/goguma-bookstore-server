package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationCreateParam;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationCreateRequestTest extends IntegrationTest {

  @Test
  @DisplayName("신간 등록 요청 객체에서 파람 객체로 변환한다.")
  void toTest() {
    // Given
    RegistrationCreateRequest request =
        new RegistrationCreateRequest(
            "유라시아 여행기",
            "대충 베트남에서 시작해서 영국까지",
            "이 편지는 영국에서 시작하여 대충 누구 손을 거쳐서 어쩌구 저쩌구 거시기 뭐시기",
            List.of(Category.NOVEL, Category.CARTOON),
            "출판사J",
            "978-89-12345-67-8",
            200,
            100,
            "양장본",
            500L,
            20_000L,
            10L,
            true);

    // When
    RegistrationCreateParam result = request.to();

    // Then
    assertAll(
        () -> assertThat(result.title()).isEqualTo(request.title()),
        () -> assertThat(result.oneLiner()).isEqualTo(request.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(request.summary()),
        () ->
            assertThat(result.categories())
                .containsExactlyInAnyOrderElementsOf(request.categories()),
        () -> assertThat(result.publisher()).isEqualTo(request.publisher()),
        () -> assertThat(result.isbn()).isEqualTo(request.isbn()),
        () -> assertThat(result.price()).isEqualTo(request.price()),
        () -> assertThat(result.stockCount()).isEqualTo(request.stockCount()),
        () -> assertThat(result.coverType()).isEqualTo(request.coverType()),
        () -> assertThat(result.pageCount()).isEqualTo(request.pageCount()),
        () -> assertThat(result.price()).isEqualTo(request.price()),
        () -> assertThat(result.stockCount()).isEqualTo(request.stockCount()),
        () -> assertThat(result.promotion()).isEqualTo(request.promotion()));
  }
}
