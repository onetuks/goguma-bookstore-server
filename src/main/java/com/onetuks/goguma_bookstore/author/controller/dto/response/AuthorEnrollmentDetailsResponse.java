package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEnrollmentDetailsResult;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

public record AuthorEnrollmentDetailsResponse(
    long authorId,
    long memberId,
    RoleType roleType,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instragramUrl,
    String escrowServiceUrl,
    String mailOrderSalesUrl,
    boolean enrollmentPassed,
    LocalDateTime enrollmentAt) {

  public static AuthorEnrollmentDetailsResponse from(AuthorEnrollmentDetailsResult result) {
    return new AuthorEnrollmentDetailsResponse(
        result.authorId(),
        result.memberId(),
        result.roleType(),
        result.profileImgUrl(),
        result.nickname(),
        result.introduction(),
        result.instagramUrl(),
        result.escrowServiceUrl(),
        result.mailOrderSalesUrl(),
        result.enrollmentPassed(),
        result.enrollmentAt());
  }

  public record AuthorEnrollmentDetailsResponses(Page<AuthorEnrollmentDetailsResponse> responses) {

    public static AuthorEnrollmentDetailsResponses from(
        Page<AuthorEnrollmentDetailsResult> results) {
      return new AuthorEnrollmentDetailsResponses(
          results.map(AuthorEnrollmentDetailsResponse::from));
    }
  }
}
