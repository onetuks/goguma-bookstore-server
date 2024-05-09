package com.onetuks.modulescm.author.service.dto.result;

import com.onetuks.modulepersistence.author.model.Author;

public record AuthorCreateEnrollmentResult(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
    String businessNumber,
    String mailOrderSalesNumber) {

  public static AuthorCreateEnrollmentResult from(Author author) {
    return new AuthorCreateEnrollmentResult(
        author.getAuthorId(),
        author.getProfileImgUrl(),
        author.getNickname(),
        author.getIntroduction(),
        author.getInstagramUrl(),
        author.getBusinessNumber(),
        author.getMailOrderSalesNumber());
  }
}
