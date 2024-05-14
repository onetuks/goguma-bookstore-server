package com.onetuks.scmdomain.book.param;

import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.scmdomain.registration.param.RegistrationEditParam;
import java.util.List;

public record BookEditParam(
    String oneLiner,
    String summary,
    List<Category> categories,
    long price,
    int salesRate,
    boolean isPromotion,
    long stockCount) {

}
