package com.onetuks.goguma_bookstore.member.controller.dto.request;

import com.onetuks.goguma_bookstore.member.service.dto.param.MemberProfileEditParam;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MemberProfileEditRequest(
    @NotBlank @Length(min = 1, max = 20) String nickname,
    @NotNull Boolean alarmPermission,
    @NotBlank String defaultAddress,
    @NotBlank String defaultAddressDetail,
    @NotBlank String defaultCashReceiptType,
    @NotBlank String defaultCashReceiptNumber) {

  public MemberProfileEditParam to() {
    return new MemberProfileEditParam(
        nickname(),
        alarmPermission(),
        defaultAddress(),
        defaultAddressDetail(),
        CashReceiptType.of(defaultCashReceiptType()),
        defaultCashReceiptNumber());
  }
}