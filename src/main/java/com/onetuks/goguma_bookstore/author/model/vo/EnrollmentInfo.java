package com.onetuks.goguma_bookstore.author.model.vo;

import com.onetuks.goguma_bookstore.global.vo.file.EscrowServiceFile;
import com.onetuks.goguma_bookstore.global.vo.file.MailOrderSalesFile;
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

  @Embedded private EscrowServiceFile escrowServiceFile;

  @Embedded private MailOrderSalesFile mailOrderSalesFile;

  @Column(name = "enrollment_passed", nullable = false)
  private Boolean enrollmentPassed;

  @Column(name = "enrollment_at", nullable = false)
  private LocalDateTime enrollmentAt;

  @Builder
  public EnrollmentInfo(
      EscrowServiceFile escrowServiceFile,
      MailOrderSalesFile mailOrderSalesFile,
      Boolean enrollmentPassed,
      LocalDateTime enrollmentAt) {
    this.escrowServiceFile = escrowServiceFile;
    this.mailOrderSalesFile = mailOrderSalesFile;
    this.enrollmentPassed = enrollmentPassed;
    this.enrollmentAt = enrollmentAt;
  }

  public EnrollmentInfo setEscrowServiceFile(EscrowServiceFile escrowServiceFile) {
    return EnrollmentInfo.builder()
        .escrowServiceFile(escrowServiceFile)
        .mailOrderSalesFile(getMailOrderSalesFile())
        .enrollmentPassed(getEnrollmentPassed())
        .enrollmentAt(LocalDateTime.now())
        .build();
  }

  public EnrollmentInfo setMailOrderSalesFile(MailOrderSalesFile mailOrderSalesFile) {
    return EnrollmentInfo.builder()
        .escrowServiceFile(getEscrowServiceFile())
        .mailOrderSalesFile(mailOrderSalesFile)
        .enrollmentPassed(getEnrollmentPassed())
        .enrollmentAt(LocalDateTime.now())
        .build();
  }

  public EnrollmentInfo convertEnrollmentPassedStatus() {
    return EnrollmentInfo.builder()
        .escrowServiceFile(getEscrowServiceFile())
        .mailOrderSalesFile(getMailOrderSalesFile())
        .enrollmentPassed(!getEnrollmentPassed())
        .enrollmentAt(LocalDateTime.now())
        .build();
  }
}
