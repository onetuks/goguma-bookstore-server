package com.onetuks.coredomain.author.model.vo;

import java.time.LocalDateTime;

public record EnrollmentInfo(
    String businessNumber,
    String mailOrderSalesNumber,
    Boolean isEnrollmentPassed,
    LocalDateTime enrollmentAt
) {

  public EnrollmentInfo convertEnrollmentPassed() {
    return new EnrollmentInfo(
        businessNumber(),
        mailOrderSalesNumber(),
        !isEnrollmentPassed(),
        enrollmentAt()
    );
  }
}
