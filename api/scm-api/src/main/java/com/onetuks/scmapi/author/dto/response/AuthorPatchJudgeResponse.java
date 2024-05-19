package com.onetuks.scmapi.author.dto.response;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coreobj.enums.member.RoleType;
import java.util.List;

public record AuthorPatchJudgeResponse(
    boolean enrollmentPassed, long memberId, List<RoleType> roleTypes) {

  public static AuthorPatchJudgeResponse from(Author author) {
    return new AuthorPatchJudgeResponse(
        author.enrollmentInfo().isEnrollmentPassed(),
        author.member().memberId(),
        author.member().authInfo().roles());
  }
}
