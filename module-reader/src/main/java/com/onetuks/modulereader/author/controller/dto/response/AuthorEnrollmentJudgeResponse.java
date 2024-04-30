package com.onetuks.modulereader.author.controller.dto.response;

import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulereader.author.service.dto.result.AuthorEnrollmentJudgeResult;

public record AuthorEnrollmentJudgeResponse(
    boolean enrollmentPassed, long memberId, RoleType roleType) {

  public static AuthorEnrollmentJudgeResponse from(AuthorEnrollmentJudgeResult result) {
    return new AuthorEnrollmentJudgeResponse(
        result.enrollmentPassed(), result.memberId(), result.roleType());
  }
}
