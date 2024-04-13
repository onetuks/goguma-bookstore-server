package com.onetuks.goguma_bookstore.member.controller.dto.request;

import com.onetuks.goguma_bookstore.member.service.dto.param.MemberDefaultAddressEditParam;

public record MemberDefaultAddressEditRequest(String defaultAddress, String defaultAddressDetail) {

  public MemberDefaultAddressEditParam to() {
    return new MemberDefaultAddressEditParam(defaultAddress(), defaultAddressDetail());
  }
}
