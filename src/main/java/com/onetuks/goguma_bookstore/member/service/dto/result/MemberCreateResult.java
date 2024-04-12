package com.onetuks.goguma_bookstore.member.service.dto.result;

import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.vo.ClientProvider;
import com.onetuks.goguma_bookstore.member.vo.RoleType;

public record MemberCreateResult(
    long memberId,
    String name,
    String socialId,
    ClientProvider clientProvider,
    RoleType roleType,
    boolean isNewMember) {

  public static MemberCreateResult from(Member member, boolean isNewMember) {
    return new MemberCreateResult(
        member.getMemberId(),
        member.getName(),
        member.getSocialId(),
        member.getClientProvider(),
        member.getRoleType(),
        isNewMember);
  }
}
