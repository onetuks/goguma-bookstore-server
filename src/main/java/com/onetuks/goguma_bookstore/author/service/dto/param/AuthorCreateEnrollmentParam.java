package com.onetuks.goguma_bookstore.author.service.dto.param;

public record AuthorCreateEnrollmentParam(
    String nickname,
    String introduction,
    String instagramUrl,
    String businessNumber,
    String mailOrderSalesNumber) {}
