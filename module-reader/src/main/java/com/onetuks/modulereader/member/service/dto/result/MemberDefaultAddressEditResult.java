package com.onetuks.modulereader.member.service.dto.result;

import com.onetuks.modulepersistence.member.entity.MemberEntity;

public record MemberDefaultAddressEditResult(String defaultAddress, String defaultAddressDetail) {

  public static MemberDefaultAddressEditResult from(MemberEntity memberEntity) {
    return new MemberDefaultAddressEditResult(
        memberEntity.getDefaultAddress(), memberEntity.getDefaultAddressDetail());
  }
}
