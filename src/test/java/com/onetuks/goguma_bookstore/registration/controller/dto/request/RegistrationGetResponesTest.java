package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationGetResponse.RegistrationGetResponses;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationGetResponesTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 다건 조회 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    List<RegistrationGetResult> results =
        IntStream.range(1, 4)
            .mapToObj(
                index ->
                    new RegistrationGetResult(
                        index,
                        true,
                        "심사 통과했습니다.",
                        "제목" + index,
                        "한줄소개" + index,
                        "줄거리 요약" + index,
                        List.of(Category.NOVEL, Category.ESSEY),
                        "isbn" + index,
                        200,
                        100,
                        "책 표지 타입",
                        300L,
                        20_000L,
                        10_000L,
                        true,
                        "출판사" + index,
                        100L,
                        "표지 이미지 URL" + index,
                        List.of("상세 이미지 URL1" + index, "상세 이미지 URL2" + index),
                        List.of("미리보기 URL1" + index, "미리보기 URL2" + index),
                        "샘플 URL" + index))
            .toList();

    // When
    RegistrationGetResponses responses = RegistrationGetResponses.from(results);

    // Then
    assertThat(responses.responses())
        .hasSize(results.size())
        .allSatisfy(
            result -> {
              assertThat(result.registrationId()).isPositive();
              assertThat(result.approvalResult()).isTrue();
              assertThat(result.approvalMemo()).isEqualTo(results.get(0).approvalMemo());
              assertThat(result.title()).contains(String.valueOf(result.registrationId()));
              assertThat(result.oneLiner()).contains(String.valueOf(result.registrationId()));
              assertThat(result.summary()).contains(String.valueOf(result.registrationId()));
              assertThat(result.categories()).contains(Category.NOVEL, Category.ESSEY);
              assertThat(result.isbn()).contains(String.valueOf(result.registrationId()));
              assertThat(result.height()).isEqualTo(200);
              assertThat(result.width()).isEqualTo(100);
              assertThat(result.coverType()).contains("책 표지 타입");
            });
  }
}
