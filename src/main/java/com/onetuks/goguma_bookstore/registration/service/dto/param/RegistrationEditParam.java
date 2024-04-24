package com.onetuks.goguma_bookstore.registration.service.dto.param;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import java.util.List;

public record RegistrationEditParam(
    String oneLiner,
    String summary,
    List<Category> categories,
    long regularPrice,
    long purchasePrice,
    boolean promotion,
    long stockCount) {}
