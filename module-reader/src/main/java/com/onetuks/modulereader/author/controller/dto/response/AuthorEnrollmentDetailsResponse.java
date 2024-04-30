package com.onetuks.modulereader.author.controller.dto.response;

import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulereader.author.service.dto.result.AuthorEnrollmentDetailsResult;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

public record AuthorEnrollmentDetailsResponse(
    long authorId,
    long memberId,
    RoleType roleType,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
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