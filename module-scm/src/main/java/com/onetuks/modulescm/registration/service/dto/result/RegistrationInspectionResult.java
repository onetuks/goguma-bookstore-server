package com.onetuks.modulescm.registration.service.dto.result;

import com.onetuks.modulepersistence.registration.entity.RegistrationEntity;

public record RegistrationInspectionResult(
    long registrationId, boolean approvalResult, String approvalMemo) {

  public static RegistrationInspectionResult from(RegistrationEntity registrationEntity) {
    return new RegistrationInspectionResult(
        registrationEntity.getRegistrationId(),
        registrationEntity.getApprovalResult(),
        registrationEntity.getApprovalMemo());
  }
}
