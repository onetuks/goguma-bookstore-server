package com.onetuks.scmdomain.book.param;

import com.onetuks.coreobj.enums.book.Category;
import java.util.List;

public record BookEditParam(
    String oneLiner,
    String summary,
    List<Category> categories,
    long price,
    int salesRate,
    boolean isPromotion,
    long stockCount) {}
