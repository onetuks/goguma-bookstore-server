package com.onetuks.scmapi.controller.dto.response;

import com.onetuks.dbstorage.global.vo.auth.RoleType;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentJudgeResult;
import java.util.List;

public record AuthorEnrollmentJudgeResponse(
    boolean enrollmentPassed, long memberId, List<RoleType> roleTypes) {

  public static AuthorEnrollmentJudgeResponse from(AuthorEnrollmentJudgeResult result) {
    return new AuthorEnrollmentJudgeResponse(
        result.enrollmentPassed(), result.memberId(), result.roleTypes());
  }
}