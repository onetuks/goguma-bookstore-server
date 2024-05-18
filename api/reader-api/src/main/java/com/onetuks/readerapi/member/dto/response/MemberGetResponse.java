package com.onetuks.readerapi.member.dto.response;

import com.onetuks.coredomain.member.model.Member;

public record MemberGetResponse(
    long memberId,
    String nickname,
    String profileImgUrl,
    boolean alarmPermission,
    String defaultAddress,
    String defaultAddressDetail
) {

  public static MemberGetResponse from(Member member) {
    return new MemberGetResponse(
        member.memberId(),
        member.nickname().nicknameValue(),
        member.profileImgFilePath().getUrl(),
        member.isAlarmPermitted(),
        member.defaultAddressInfo().address(),
        member.defaultAddressInfo().addressDetail()
    );
  }
}
