package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationInspectionParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationInspectionRequest(
    @NotNull Boolean approvalResult, @NotBlank String approvalMemo) {

  public RegistrationInspectionParam to() {
    return new RegistrationInspectionParam(approvalResult(), approvalMemo());
  }
}
