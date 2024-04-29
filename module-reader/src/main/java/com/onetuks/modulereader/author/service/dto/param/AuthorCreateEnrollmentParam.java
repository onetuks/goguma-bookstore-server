package com.onetuks.modulereader.author.service.dto.param;

public record AuthorCreateEnrollmentParam(
    String nickname,
    String introduction,
    String instagramUrl,
    String businessNumber,
    String mailOrderSalesNumber) {}
