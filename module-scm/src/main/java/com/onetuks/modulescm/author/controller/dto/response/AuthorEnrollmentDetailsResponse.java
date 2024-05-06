package com.onetuks.modulescm.author.controller.dto.response;

import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentDetailsResult;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record AuthorEnrollmentDetailsResponse(
    long authorId,
    long memberId,
    List<RoleType> roleTypes,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
    String businessNumber,
    String mailOrderSalesUrl,
    boolean enrollmentPassed,
    LocalDateTime enrollmentAt) {

  public static AuthorEnrollmentDetailsResponse from(AuthorEnrollmentDetailsResult result) {
    return new AuthorEnrollmentDetailsResponse(
        result.authorId(),
        result.memberId(),
        result.roleTypes(),
        result.profileImgUrl(),
        result.nickname(),
        result.introduction(),
        result.instagramUrl(),
        result.businessNumber(),
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
