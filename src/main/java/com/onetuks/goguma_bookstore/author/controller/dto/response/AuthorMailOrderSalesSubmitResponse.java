package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorMailOrderSalesSubmitResult;

public record AuthorMailOrderSalesSubmitResponse(String mailOrderSalesUrl) {

  public static AuthorMailOrderSalesSubmitResponse from(AuthorMailOrderSalesSubmitResult result) {
    return new AuthorMailOrderSalesSubmitResponse(result.mailOrderSalesUrl());
  }
}
