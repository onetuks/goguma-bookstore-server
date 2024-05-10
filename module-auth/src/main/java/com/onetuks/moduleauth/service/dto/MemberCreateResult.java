package com.onetuks.moduleauth.service.dto;

import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.embedded.AuthInfo;
import java.util.List;

public record MemberCreateResult(
    long memberId,
    String name,
    String socialId,
    ClientProvider clientProvider,
    List<RoleType> roleTypes,
    boolean isNewMember) {

  public static MemberCreateResult from(MemberEntity memberEntity, boolean isNewMember) {
    AuthInfo authInfo = memberEntity.getAuthInfo();

    return new MemberCreateResult(
        memberEntity.getMemberId(),
        authInfo.getName(),
        authInfo.getSocialId(),
        authInfo.getClientProvider(),
        authInfo.getRoleTypes(),
        isNewMember);
  }
}
