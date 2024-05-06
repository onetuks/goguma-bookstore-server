package com.onetuks.modulescm.registration.controller.dto.response;

import com.onetuks.modulescm.registration.service.dto.result.RegistrationInspectionResult;

public record RegistrationInspectionResponse(
    long registrationId, boolean approvalResult, String approvalMemo) {

  public static RegistrationInspectionResponse from(RegistrationInspectionResult result) {
    return new RegistrationInspectionResponse(
        result.registrationId(), result.approvalResult(), result.approvalMemo());
  }
}
