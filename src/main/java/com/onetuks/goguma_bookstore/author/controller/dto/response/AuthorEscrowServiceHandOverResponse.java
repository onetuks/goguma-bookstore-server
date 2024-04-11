package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEscrowServiceHandOverResult;

public record AuthorEscrowServiceHandOverResponse(String escrowServiceUrl) {

  public static AuthorEscrowServiceHandOverResponse from(AuthorEscrowServiceHandOverResult result) {
    return new AuthorEscrowServiceHandOverResponse(result.escrowServiceUrl());
  }
}
