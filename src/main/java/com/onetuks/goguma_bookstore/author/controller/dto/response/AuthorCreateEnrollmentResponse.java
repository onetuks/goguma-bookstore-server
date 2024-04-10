package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateEnrollmentResult;

public record AuthorCreateEnrollmentResponse(long authorId) {

  public static AuthorCreateEnrollmentResponse from(AuthorCreateEnrollmentResult result) {
    return new AuthorCreateEnrollmentResponse(result.authorId());
  }
}
