package com.onetuks.scmdomain.registration.param;

import com.onetuks.coreobj.enums.book.Category;
import java.util.List;

public record RegistrationCreateParam(
    String title,
    String oneLiner,
    String summary,
    List<Category> categories,
    String publisher,
    String isbn,
    int height,
    int width,
    String coverType,
    long pageCount,
    long price,
    int salesRate,
    boolean isPromotion,
    long stockCount) {

}
