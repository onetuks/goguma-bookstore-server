package com.onetuks.modulereader.member.controller.dto.request;

import com.onetuks.modulereader.member.service.dto.param.MemberDefaultAddressEditParam;
import jakarta.validation.constraints.NotBlank;

public record MemberDefaultAddressEditRequest(
    @NotBlank String defaultAddress, @NotBlank String defaultAddressDetail) {

  public MemberDefaultAddressEditParam to() {
    return new MemberDefaultAddressEditParam(defaultAddress(), defaultAddressDetail());
  }
}
