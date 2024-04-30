package com.onetuks.modulereader.book.controller.dto.request;

import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulereader.book.service.dto.param.BookEditParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record BookEditRequest(
    @NotBlank @Length(min = 1, max = 100) String oneLiner,
    @NotBlank @Length(max = 5_000) String summary,
    @NotEmpty @Size(min = 1, max = 3) List<Category> categories,
    @PositiveOrZero Long regularPrice,
    @PositiveOrZero Long purchasePrice,
    @PositiveOrZero Long stockCount,
    @NotNull Boolean promotion) {

  public BookEditParam to() {
    return new BookEditParam(
        oneLiner(),
        summary(),
        categories(),
        regularPrice(),
        purchasePrice(),
        promotion(),
        stockCount());
  }
}
