package com.onetuks.scmapi.registration.dto.response;

import com.onetuks.coredomain.registration.model.Registration;

public record RegistrationEditJudgeResponse(
    long registrationId,
    boolean approvalResult,
    String approvalMemo
) {

  public static RegistrationEditJudgeResponse from(Registration registration) {
    return new RegistrationEditJudgeResponse(
        registration.registrationId(),
        registration.approvalInfo().isApproved(),
        registration.approvalInfo().approvalMemo()
    );
  }
}
