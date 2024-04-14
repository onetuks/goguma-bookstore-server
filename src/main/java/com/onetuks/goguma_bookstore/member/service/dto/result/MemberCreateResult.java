package com.onetuks.goguma_bookstore.member.service.dto.result;

import com.onetuks.goguma_bookstore.global.vo.auth.ClientProvider;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.vo.AuthInfo;

public record MemberCreateResult(
    long memberId,
    String name,
    String socialId,
    ClientProvider clientProvider,
    RoleType roleType,
    boolean isNewMember) {

  public static MemberCreateResult from(Member member, boolean isNewMember) {
    AuthInfo authInfo = member.getAuthInfo();

    return new MemberCreateResult(
        member.getMemberId(),
        authInfo.getName(),
        authInfo.getSocialId(),
        authInfo.getClientProvider(),
        authInfo.getRoleType(),
        isNewMember);
  }
}
