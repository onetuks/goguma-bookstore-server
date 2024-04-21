package com.onetuks.goguma_bookstore.registration.service.dto.result;

import com.onetuks.goguma_bookstore.registration.model.Registration;

public record RegistrationInspectionResult(
    long registrationId, boolean approvalResult, String approvalMemo) {

  public static RegistrationInspectionResult from(Registration registration) {
    return new RegistrationInspectionResult(
        registration.getRegistrationId(),
        registration.getApprovalInfo().getApprovalResult(),
        registration.getApprovalInfo().getApprovalMemo());
  }
}
