package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentJudgeResult;
import com.onetuks.goguma_bookstore.member.vo.RoleType;

public record AuthorEnrollmentJudgeResponse(
    boolean enrollmentPassed, long memberId, RoleType roleType) {

  public static AuthorEnrollmentJudgeResponse from(AuthorEnrollmentJudgeResult result) {
    return new AuthorEnrollmentJudgeResponse(
        result.enrollmentPassed(), result.memberId(), result.roleType());
  }
}
