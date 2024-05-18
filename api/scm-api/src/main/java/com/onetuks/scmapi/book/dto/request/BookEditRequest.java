package com.onetuks.scmapi.book.dto.request;

import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.scmdomain.book.param.BookEditParam;
import jakarta.validation.constraints.Max;
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
    @PositiveOrZero Long price,
    @PositiveOrZero @Max(value = 10) Integer salesRate,
    @NotNull Boolean isPromotion,
    @PositiveOrZero Long stockCount) {

  public BookEditParam to() {
    return new BookEditParam(
        oneLiner(),
        summary(),
        categories(),
        price(),
        salesRate(),
        isPromotion(),
        stockCount()
    );
  }
}
