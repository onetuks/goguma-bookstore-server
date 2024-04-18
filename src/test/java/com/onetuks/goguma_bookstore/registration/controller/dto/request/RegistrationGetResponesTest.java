package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.registration.controller.dto.response.RegistrationGetResponse.RegistrationGetResponses;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegistrationGetResponesTest extends IntegrationTest {

  @Test
  @DisplayName("신간등록 다건 조회 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    List<RegistrationGetResult> results =
        List.of(
            new RegistrationGetResult(
                1L,
                true,
                "memo1",
                "coverImgUrl1",
                "title1",
                "summary1",
                1001L,
                11L,
                "isbn",
                "publisher",
                true,
                "sampleUrl1"),
            new RegistrationGetResult(
                2L,
                false,
                "memo2",
                "coverImgUrl2",
                "title2",
                "summary2",
                1002L,
                12L,
                "isbn",
                "publisher",
                true,
                "sampleUrl2"),
            new RegistrationGetResult(
                3L,
                true,
                "mem3o",
                "coverImgUrl3",
                "title3",
                "summary3",
                1003L,
                13L,
                "isbn",
                "publisher",
                true,
                "sampleUrl3"));

    // When
    RegistrationGetResponses responses = RegistrationGetResponses.from(results);

    // Then
    assertThat(responses.responses())
        .hasSize(results.size())
        .allSatisfy(
            result -> {
              assertThat(result.approvalMemo()).contains(String.valueOf(result.registrationId()));
              assertThat(result.sampleUrl()).contains(String.valueOf(result.registrationId()));
            });
  }
}
