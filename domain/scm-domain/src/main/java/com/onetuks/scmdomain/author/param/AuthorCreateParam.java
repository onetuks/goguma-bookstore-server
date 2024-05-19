package com.onetuks.scmdomain.author.param;

public record AuthorCreateParam(
    String nickname,
    String introduction,
    String instagramUrl,
    String businessNumber,
    String mailOrderSalesNumber) {}
