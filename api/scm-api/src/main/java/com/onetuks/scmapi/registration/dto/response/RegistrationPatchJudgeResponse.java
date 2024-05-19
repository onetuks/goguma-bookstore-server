package com.onetuks.scmapi.registration.dto.response;

import com.onetuks.coredomain.registration.model.Registration;

public record RegistrationPatchJudgeResponse(
    long registrationId, boolean approvalResult, String approvalMemo) {

  public static RegistrationPatchJudgeResponse from(Registration registration) {
    return new RegistrationPatchJudgeResponse(
        registration.registrationId(),
        registration.approvalInfo().isApproved(),
        registration.approvalInfo().approvalMemo());
  }
}
