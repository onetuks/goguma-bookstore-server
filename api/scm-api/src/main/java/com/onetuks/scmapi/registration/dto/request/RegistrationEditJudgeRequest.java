package com.onetuks.scmapi.registration.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationEditJudgeRequest(
    @NotNull Boolean approvalResult, @NotBlank String approvalMemo) {}
