package com.onetuks.modulereader.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.author.service.dto.result.AuthorEnrollmentJudgeResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorEnrollmentJudgeResponseTest extends IntegrationTest {

  @Test
  @DisplayName("입점 심사 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    boolean enrollmentPassed = true;
    long memberId = 1_000L;
    List<RoleType> roleTypes = List.of(RoleType.AUTHOR);
    AuthorEnrollmentJudgeResult judgeResult =
        new AuthorEnrollmentJudgeResult(enrollmentPassed, memberId, roleTypes);

    // When
    AuthorEnrollmentJudgeResponse result = AuthorEnrollmentJudgeResponse.from(judgeResult);

    // Then
    assertAll(
        () -> assertThat(result.enrollmentPassed()).isTrue(),
        () -> assertThat(result.memberId()).isEqualTo(memberId),
        () -> assertThat(result.roleTypes()).contains(RoleType.AUTHOR));
  }
}
