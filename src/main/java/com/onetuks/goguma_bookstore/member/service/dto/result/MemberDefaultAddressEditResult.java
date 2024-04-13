package com.onetuks.goguma_bookstore.member.service.dto.result;

import com.onetuks.goguma_bookstore.member.model.Member;

public record MemberDefaultAddressEditResult(String defaultAddress, String defaultAddressDetail) {

  public static MemberDefaultAddressEditResult from(Member member) {
    return new MemberDefaultAddressEditResult(
        member.getDefaultAddress(), member.getDefaultAddressDetail());
  }
}
