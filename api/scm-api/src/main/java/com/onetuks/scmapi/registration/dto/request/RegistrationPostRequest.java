package com.onetuks.scmapi.registration.dto.request;

import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.scmdomain.registration.param.RegistrationCreateParam;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record RegistrationPostRequest(
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
    @PositiveOrZero Long price,
    @PositiveOrZero @Max(value = 10) Integer salesRate,
    @NotNull Boolean isPromotion,
    @PositiveOrZero Long stockCount) {

  public RegistrationCreateParam to() {
    return new RegistrationCreateParam(
        title(),
        oneLiner(),
        summary(),
        categories(),
        publisher(),
        isbn(),
        height(),
        width(),
        coverType(),
        pageCount(),
        price(),
        salesRate(),
        isPromotion(),
        stockCount());
  }
}
