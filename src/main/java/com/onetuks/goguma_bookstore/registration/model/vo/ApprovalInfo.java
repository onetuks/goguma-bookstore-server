package com.onetuks.goguma_bookstore.registration.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
    this.approvalResult = approvalResult;
    this.approvalMemo = approvalMemo;
  }
}