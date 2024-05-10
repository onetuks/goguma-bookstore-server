package com.onetuks.scmapi.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationInspectionRequest(
    @NotNull Boolean approvalResult, @NotBlank String approvalMemo) {}
