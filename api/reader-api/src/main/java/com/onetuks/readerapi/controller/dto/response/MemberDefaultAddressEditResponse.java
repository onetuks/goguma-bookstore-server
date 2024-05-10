package com.onetuks.readerapi.controller.dto.response;

import com.onetuks.modulereader.member.service.dto.result.MemberDefaultAddressEditResult;

public record MemberDefaultAddressEditResponse(String defaultAddress, String defaultAddressDetail) {

  public static MemberDefaultAddressEditResponse from(MemberDefaultAddressEditResult result) {
    return new MemberDefaultAddressEditResponse(
        result.defaultAddress(), result.defaultAddressDetail());
  }
}
