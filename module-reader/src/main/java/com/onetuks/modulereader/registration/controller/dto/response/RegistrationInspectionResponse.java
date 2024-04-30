package com.onetuks.modulereader.registration.controller.dto.response;

import com.onetuks.modulereader.registration.service.dto.result.RegistrationInspectionResult;

public record RegistrationInspectionResponse(
    long registrationId, boolean approvalResult, String approvalMemo) {

  public static RegistrationInspectionResponse from(RegistrationInspectionResult result) {
    return new RegistrationInspectionResponse(
        result.registrationId(), result.approvalResult(), result.approvalMemo());
  }
}