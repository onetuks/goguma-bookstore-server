package com.onetuks.moduleauth.service.dto;

import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.vo.AuthInfo;
import java.util.List;

public record MemberCreateResult(
    long memberId,
    String name,
    String socialId,
    ClientProvider clientProvider,
    List<RoleType> roleTypes,
    boolean isNewMember) {

  public static MemberCreateResult from(Member member, boolean isNewMember) {
    AuthInfo authInfo = member.getAuthInfo();

    return new MemberCreateResult(
        member.getMemberId(),
        authInfo.getName(),
        authInfo.getSocialId(),
        authInfo.getClientProvider(),
        authInfo.getRoleTypes(),
        isNewMember);
  }
}
