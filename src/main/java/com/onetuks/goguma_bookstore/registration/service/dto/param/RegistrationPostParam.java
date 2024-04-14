package com.onetuks.goguma_bookstore.registration.service.dto.param;

public record RegistrationPostParam(
    String title,
    String summary,
    long price,
    long stockCount,
    String isbn,
    String publisher,
    boolean promotion) {}
