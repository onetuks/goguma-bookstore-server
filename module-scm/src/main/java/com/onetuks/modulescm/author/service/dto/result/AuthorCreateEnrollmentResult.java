package com.onetuks.modulescm.author.service.dto.result;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;

public record AuthorCreateEnrollmentResult(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
    String businessNumber,
    String mailOrderSalesNumber) {

  public static AuthorCreateEnrollmentResult from(AuthorEntity authorEntity) {
    return new AuthorCreateEnrollmentResult(
        authorEntity.getAuthorId(),
        authorEntity.getProfileImgUrl(),
        authorEntity.getNickname(),
        authorEntity.getIntroduction(),
        authorEntity.getInstagramUrl(),
        authorEntity.getBusinessNumber(),
        authorEntity.getMailOrderSalesNumber());
  }
}
