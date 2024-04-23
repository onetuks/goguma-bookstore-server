package com.onetuks.goguma_bookstore.book.service.dto.param;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationEditParam;
import java.util.List;

public record BookEditParam(
    String oneLiner,
    String summary,
    List<Category> categories,
    Long regularPrice,
    Long purchasePrice,
    Boolean promotion,
    Long stockCount) {

  public RegistrationEditParam to() {
    return new RegistrationEditParam(
        oneLiner(),
        summary(),
        categories(),
        regularPrice(),
        purchasePrice(),
        promotion(),
        stockCount());
  }
}
