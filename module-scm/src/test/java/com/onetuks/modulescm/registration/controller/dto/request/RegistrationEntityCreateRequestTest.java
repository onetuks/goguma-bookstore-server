package com.onetuks.modulescm.registration.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulescm.ScmIntegrationTest;
import com.onetuks.modulescm.registration.service.dto.param.RegistrationCreateParam;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationEntityCreateRequestTest extends ScmIntegrationTest {

  @Test
  @DisplayName("신간등록 수정 요청 객체를 파람 객체로 변환한다.")
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
            10_000L,
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
        () -> assertThat(result.height()).isEqualTo(request.height()),
        () -> assertThat(result.width()).isEqualTo(request.width()),
        () -> assertThat(result.stockCount()).isEqualTo(request.stockCount()),
        () -> assertThat(result.coverType()).isEqualTo(request.coverType()),
        () -> assertThat(result.pageCount()).isEqualTo(request.pageCount()),
        () -> assertThat(result.purchasePrice()).isEqualTo(request.purchasePrice()),
        () -> assertThat(result.regularPrice()).isEqualTo(request.regularPrice()),
        () -> assertThat(result.stockCount()).isEqualTo(request.stockCount()),
        () -> assertThat(result.promotion()).isEqualTo(request.promotion()));
  }
}
