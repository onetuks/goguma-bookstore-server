package com.onetuks.modulereader.member.service.dto.result;

import com.onetuks.modulepersistence.member.model.Member;

public record MemberDefaultAddressEditResult(String defaultAddress, String defaultAddressDetail) {

  public static MemberDefaultAddressEditResult from(Member member) {
    return new MemberDefaultAddressEditResult(
        member.getDefaultAddress(), member.getDefaultAddressDetail());
  }
}
