package com.onetuks.goguma_bookstore.member.controller.dto.request;

import com.onetuks.goguma_bookstore.member.service.dto.param.MemberDefaultAddressEditParam;
import jakarta.validation.constraints.NotBlank;

public record MemberDefaultAddressEditRequest(
    @NotBlank String defaultAddress, @NotBlank String defaultAddressDetail) {

  public MemberDefaultAddressEditParam to() {
    return new MemberDefaultAddressEditParam(defaultAddress(), defaultAddressDetail());
  }
}
