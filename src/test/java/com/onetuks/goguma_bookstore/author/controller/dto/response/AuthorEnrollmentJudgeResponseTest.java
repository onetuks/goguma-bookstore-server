package com.onetuks.goguma_bookstore.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentJudgeResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorEnrollmentJudgeResponseTest {

  @Test
  @DisplayName("입점 심사 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    boolean enrollmentPassed = true;
    long memberId = 1_000L;
    RoleType roleType = RoleType.AUTHOR;
    AuthorEnrollmentJudgeResult judgeResult =
        new AuthorEnrollmentJudgeResult(enrollmentPassed, memberId, roleType);

    // When
    AuthorEnrollmentJudgeResponse result = AuthorEnrollmentJudgeResponse.from(judgeResult);

    // Then
    assertAll(
        () -> assertThat(result.enrollmentPassed()).isTrue(),
        () -> assertThat(result.memberId()).isEqualTo(memberId),
        () -> assertThat(result.roleType()).isEqualTo(RoleType.AUTHOR));
  }
}
