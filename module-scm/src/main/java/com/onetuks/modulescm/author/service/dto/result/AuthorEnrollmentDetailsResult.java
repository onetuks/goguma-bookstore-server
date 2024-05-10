package com.onetuks.modulescm.author.service.dto.result;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import java.time.LocalDateTime;
import java.util.List;

public record AuthorEnrollmentDetailsResult(
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

  public static AuthorEnrollmentDetailsResult from(AuthorEntity authorEntity) {
    return new AuthorEnrollmentDetailsResult(
        authorEntity.getAuthorId(),
        authorEntity.getMemberEntity().getMemberId(),
        authorEntity.getMemberEntity().getRoleTypes(),
        authorEntity.getProfileImgUrl(),
        authorEntity.getNickname(),
        authorEntity.getIntroduction(),
        authorEntity.getInstagramUrl(),
        authorEntity.getBusinessNumber(),
        authorEntity.getMailOrderSalesNumber(),
        authorEntity.getEnrollmentPassed(),
        authorEntity.getEnrollmentAt());
  }
}
