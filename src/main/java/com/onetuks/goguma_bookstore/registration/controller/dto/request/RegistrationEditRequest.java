package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.model.vo.PageSizeInfo;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationEditParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record RegistrationEditRequest(
    @NotBlank @Length(min = 1, max = 20) String title,
    @NotBlank @Length(min = 1, max = 100) String oneLiner,
    @NotBlank @Length(max = 5_000) String summary,
    @NotEmpty @Size(min = 1, max = 3) List<Category> categories,
    @NotBlank @Length(min = 1, max = 20) String publisher,
    @NotBlank String isbn,
    @Positive Integer height,
    @Positive Integer width,
    @NotBlank String coverType,
    @Positive Long pageCount,
    @PositiveOrZero Long price,
    @PositiveOrZero Long stockCount,
    @NotNull Boolean promotion) {

  public RegistrationEditParam to() {
    return new RegistrationEditParam(
        title(),
        oneLiner(),
        summary(),
        categories(),
        publisher(),
        isbn(),
        new PageSizeInfo(height, width),
        coverType(),
        pageCount(),
        price(),
        stockCount(),
        promotion());
  }
}
