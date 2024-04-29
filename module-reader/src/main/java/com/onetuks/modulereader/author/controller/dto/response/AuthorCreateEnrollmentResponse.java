package com.onetuks.modulereader.author.controller.dto.response;

import com.onetuks.modulereader.author.service.dto.result.AuthorCreateEnrollmentResult;

public record AuthorCreateEnrollmentResponse(long authorId) {

  public static AuthorCreateEnrollmentResponse from(AuthorCreateEnrollmentResult result) {
    return new AuthorCreateEnrollmentResponse(result.authorId());
  }
}
