package com.onetuks.readerapi.member.dto.request;

import com.onetuks.readerdomain.member.param.MemberPatchParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MemberPatchRequest(
    @NotBlank @Length(min = 1, max = 20) String nickname,
    @NotNull Boolean alarmPermission,
    @NotBlank @Length(min = 1, max = 20) String defaultAddress,
    @NotNull String defaultAddressDetail) {

  public MemberPatchParam to() {
    return new MemberPatchParam(
        nickname(), alarmPermission(), defaultAddress(), defaultAddressDetail());
  }
}
