package com.onetuks.goguma_bookstore.registration.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ApprovalInfo {

  @Column(name = "approval_result", nullable = false)
  private Boolean approvalResult;

  @Column(name = "approval_memo", nullable = false)
  private String approvalMemo;

  @Builder
  public ApprovalInfo(Boolean approvalResult, String approvalMemo) {
    this.approvalResult = Objects.requireNonNullElse(approvalResult, false);
    this.approvalMemo = Objects.requireNonNullElse(approvalMemo, "신간 등록 검수 중입니다.");
  }
}
