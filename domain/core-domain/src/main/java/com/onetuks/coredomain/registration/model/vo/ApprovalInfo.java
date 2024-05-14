package com.onetuks.coredomain.registration.model.vo;

import java.util.Objects;

public record ApprovalInfo(
    boolean isApproved,
    String approvalMemo
) {

  public static final String WAIT_APPROVAL = "신간 등록 검수 중입니다.";
  public static final String APPROVED = "신간 등록이 승인되었습니다.";
  public static final String REJECTED = "신간 등록이 거절되었습니다.";

  public static ApprovalInfo init() {
    return new ApprovalInfo(false, WAIT_APPROVAL);
  }

  public static ApprovalInfo changeApprovalInfo(boolean isApproved, String approvalMemo) {
    if (isApproved) {
      return new ApprovalInfo(true, APPROVED);
    }
    return new ApprovalInfo(false, Objects.requireNonNullElse(approvalMemo, REJECTED));
  }
}
