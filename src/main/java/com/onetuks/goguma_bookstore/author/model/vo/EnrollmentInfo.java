package com.onetuks.goguma_bookstore.author.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class EnrollmentInfo {

  @Embedded private EscrowService escrowService;

  @Embedded private MailOrderSales mailOrderSales;

  @Column(name = "enrollment_passed", nullable = false)
  private Boolean enrollmentPassed;

  @Column(name = "enrollment_at", nullable = false)
  private LocalDateTime enrollmentAt;

  @Builder
  public EnrollmentInfo(
      String escrowServiceUri,
      String mailOrderSalesUri,
      Boolean enrollmentPassed,
      LocalDateTime enrollmentAt) {
    this.escrowService = new EscrowService(escrowServiceUri);
    this.mailOrderSales = new MailOrderSales(mailOrderSalesUri);
    this.enrollmentPassed = enrollmentPassed;
    this.enrollmentAt = enrollmentAt;
  }

  public EnrollmentInfo setEscrowService(String escrowServiceUri) {
    return new EnrollmentInfo(
        escrowServiceUri,
        this.mailOrderSales.getMailOrderSalesUri(),
        this.enrollmentPassed,
        LocalDateTime.now());
  }

  public EnrollmentInfo setMailOrderSales(String mailOrderSalesUri) {
    return new EnrollmentInfo(
        this.escrowService.getEscrowServiceUri(),
        mailOrderSalesUri,
        this.enrollmentPassed,
        LocalDateTime.now());
  }

  public EnrollmentInfo convertEnrollmentPassedStatus() {
    return new EnrollmentInfo(
        this.escrowService.getEscrowServiceUri(),
        this.mailOrderSales.getMailOrderSalesUri(),
        !this.enrollmentPassed,
        LocalDateTime.now());
  }
}
