package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import java.time.LocalDateTime;

public record AuthorEnrollmentDetailsResult(
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

  public static AuthorEnrollmentDetailsResult from(Author author) {
    return new AuthorEnrollmentDetailsResult(
        author.getAuthorId(),
        author.getMember().getMemberId(),
        author.getMember().getRoleType(),
        author.getProfileImgUrl(),
        author.getNickname(),
        author.getIntroduction(),
        author.getInstagramUrl(),
        author.getEnrollmentInfo().getBusinessNumber(),
        author.getEnrollmentInfo().getMailOrderSalesNumber(),
        author.getEnrollmentInfo().getEnrollmentPassed(),
        author.getEnrollmentInfo().getEnrollmentAt());
  }
}
