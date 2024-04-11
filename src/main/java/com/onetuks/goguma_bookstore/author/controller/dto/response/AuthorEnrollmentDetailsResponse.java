package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;

public record AuthorEnrollmentDetailsResponse(
    long authorId,
    long memberId,
    RoleType roleType,
    String profileImgUrl,
    String nickname,
    String introduction,
    String escrowServiceUrl,
    String mailOrderSalesUrl,
    boolean enrollmentPassed) {

  public static AuthorEnrollmentDetailsResponse from(AuthorEnrollmentDetailsResult result) {
    return new AuthorEnrollmentDetailsResponse(
        result.authorId(),
        result.memberId(),
        result.roleType(),
        result.profileImgUrl(),
        result.nickname(),
        result.introduction(),
        result.escrowServiceUrl(),
        result.mailOrderSalesUrl(),
        result.enrollmentPassed());
  }
}
