package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationIsbnGetResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationIsbnGetResult.IsbnDataResult;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationIsbnGetResponseTest extends IntegrationTest {

  private final String publisher = "출판사";
  private final String formDetail = "양장본";
  private final String isbn = "9788999278488";

  @Test
  @DisplayName("ISBN 정보를 RegistrationIsbnGetResponse로 변환한다.")
  void fromTest() {
    // Given
    String bookSize = "100x200";
    RegistrationIsbnGetResult IsbnResult = getRegistrationIsbnGetResult(bookSize);

    // When
    RegistrationIsbnGetResponse result = RegistrationIsbnGetResponse.from(IsbnResult);

    // Then
    assertAll(
        () -> assertThat(result.isbn()).isEqualTo(isbn),
        () -> assertThat(result.publisher()).isEqualTo(publisher),
        () -> assertThat(result.coverType()).isEqualTo(formDetail),
        () -> assertThat(result.pageCount()).isEqualTo(200),
        () -> assertThat(result.height()).isEqualTo(200),
        () -> assertThat(result.width()).isEqualTo(100));
  }

  @Test
  @DisplayName("ISBN 정보를 RegistrationIsbnGetResponse로 변환 시 판형 정보가 주어지지 않으면 null로 변환한다.")
  void fromTest_BlankBookSizeInfo_NullTest() {
    // Given
    String bookSize = "";
    RegistrationIsbnGetResult IsbnResult = getRegistrationIsbnGetResult(bookSize);

    // When
    RegistrationIsbnGetResponse result = RegistrationIsbnGetResponse.from(IsbnResult);

    // Then
    assertAll(
        () -> assertThat(result.isbn()).isEqualTo(isbn),
        () -> assertThat(result.publisher()).isEqualTo(publisher),
        () -> assertThat(result.coverType()).isEqualTo(formDetail),
        () -> assertThat(result.pageCount()).isEqualTo(200),
        () -> assertThat(result.height()).isNull(),
        () -> assertThat(result.width()).isNull());
  }

  private @NotNull RegistrationIsbnGetResult getRegistrationIsbnGetResult(String bookSize) {
    String title = "책 제목";
    String pageCount = "200 p.";
    return new RegistrationIsbnGetResult(
        "1",
        List.of(
            new IsbnDataResult(
                publisher,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                bookSize,
                "",
                "",
                "",
                "",
                formDetail,
                pageCount,
                "",
                "",
                isbn,
                "",
                "",
                "",
                "",
                "",
                "",
                title,
                "",
                "",
                "")),
        "1");
  }
}
