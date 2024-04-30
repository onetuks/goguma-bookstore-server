package com.onetuks.modulereader.registration.controller.dto.request;

import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulereader.registration.service.dto.param.RegistrationCreateParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record RegistrationCreateRequest(
    @NotBlank @Length(min = 1, max = 20) String title,
    @NotBlank @Length(min = 1, max = 100) String oneLiner,
    @NotBlank @Length(max = 5_000) String summary,
    @NotEmpty @Size(min = 1, max = 3) List<Category> categories,
    @NotBlank @Length(min = 1, max = 20) String publisher,
    @NotBlank @Length(min = 13, max = 13) @Pattern(regexp = "^\\d+$") String isbn,
    @Positive Integer height,
    @Positive Integer width,
    @NotBlank String coverType,
    @Positive Long pageCount,
    @PositiveOrZero Long regularPrice,
    @PositiveOrZero Long purchasePrice,
    @PositiveOrZero Long stockCount,
    @NotNull Boolean promotion) {

  public RegistrationCreateParam to() {
    return new RegistrationCreateParam(
        title(),
        oneLiner(),
        summary(),
        categories(),
        isbn(),
        height(),
        width(),
        coverType(),
        pageCount(),
        regularPrice(),
        purchasePrice(),
        promotion(),
        publisher(),
        stockCount());
  }
}
