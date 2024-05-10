package com.onetuks.readerapi.controller.dto.request;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulereader.member.service.dto.param.MemberProfileEditParam;
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
