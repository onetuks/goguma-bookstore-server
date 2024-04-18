package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationEditParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public record RegistrationEditRequest(
    @NotBlank @Length(min = 1, max = 20) String title,
    @NotBlank @Length(max = 5_000) String summary,
    @PositiveOrZero Long price,
    @PositiveOrZero Long stockCount,
    @NotBlank String isbn,
    @NotBlank @Length(min = 1, max = 20) String publisher,
    @NotNull Boolean promotion) {

  public RegistrationEditParam to() {
    return new RegistrationEditParam(
        title(), summary(), price(), stockCount(), isbn(), publisher(), promotion());
  }
}
