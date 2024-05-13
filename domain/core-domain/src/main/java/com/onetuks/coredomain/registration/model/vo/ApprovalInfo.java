package com.onetuks.coredomain.registration.model.vo;

public record ApprovalInfo(
    boolean isApproved,
    String approvalMemo
) {

  public static ApprovalInfo init() {
    return new ApprovalInfo(false, "신간 등록 검수 중입니다.");
  }
}
