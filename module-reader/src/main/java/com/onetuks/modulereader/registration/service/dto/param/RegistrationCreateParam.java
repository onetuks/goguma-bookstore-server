package com.onetuks.modulereader.registration.service.dto.param;

import com.onetuks.modulepersistence.book.vo.Category;
import java.util.List;

public record RegistrationCreateParam(
    String title,
    String oneLiner,
    String summary,
    List<Category> categories,
    String isbn,
    int height,
    int width,
    String coverType,
    long pageCount,
    long regularPrice,
    long purchasePrice,
    boolean promotion,
    String publisher,
    long stockCount) {}
