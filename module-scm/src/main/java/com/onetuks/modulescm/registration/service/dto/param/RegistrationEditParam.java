package com.onetuks.modulescm.registration.service.dto.param;

import com.onetuks.modulepersistence.book.vo.Category;
import java.util.List;

public record RegistrationEditParam(
    String oneLiner,
    String summary,
    List<Category> categories,
    long regularPrice,
    long purchasePrice,
    boolean promotion,
    long stockCount) {}
