package com.onetuks.goguma_bookstore.author.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class EnrollmentInfo {

  @Column(name = "business_number", nullable = false, unique = true)
  private String businessNumber;

  @Column(name = "mail_order_sales_number", nullable = false, unique = true)
  private String mailOrderSalesNumber;

  @Column(name = "enrollment_passed", nullable = false)
  private Boolean enrollmentPassed;

  @Column(name = "enrollment_at", nullable = false)
  private LocalDateTime enrollmentAt;

  @Builder
  public EnrollmentInfo(
      String businessNumber,
      String mailOrderSalesNumber,
      Boolean enrollmentPassed,
      LocalDateTime enrollmentAt) {
    this.businessNumber = businessNumber;
    this.mailOrderSalesNumber = mailOrderSalesNumber;
    this.enrollmentPassed = Objects.requireNonNullElse(enrollmentPassed, false);
    this.enrollmentAt = Objects.requireNonNullElse(enrollmentAt, LocalDateTime.now());
  }

  public EnrollmentInfo convertEnrollmentPassedStatus() {
    return EnrollmentInfo.builder()
        .businessNumber(getBusinessNumber())
        .mailOrderSalesNumber(getMailOrderSalesNumber())
        .enrollmentPassed(!getEnrollmentPassed())
        .enrollmentAt(LocalDateTime.now())
        .build();
  }
}
