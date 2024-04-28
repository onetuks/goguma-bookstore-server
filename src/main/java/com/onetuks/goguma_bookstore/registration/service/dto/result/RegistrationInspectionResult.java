package com.onetuks.goguma_bookstore.registration.service.dto.result;

import com.onetuks.modulepersistence.registration.model.Registration;

public record RegistrationInspectionResult(
    long registrationId, boolean approvalResult, String approvalMemo) {

  public static RegistrationInspectionResult from(Registration registration) {
    return new RegistrationInspectionResult(
        registration.getRegistrationId(),
        registration.getApprovalResult(),
        registration.getApprovalMemo());
  }
}
