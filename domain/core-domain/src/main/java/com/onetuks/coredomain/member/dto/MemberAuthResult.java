package com.onetuks.coredomain.member.dto;

import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.coreobj.enums.member.RoleType;
import java.util.List;

public record MemberAuthResult(
    long memberId,
    String name,
    String socialId,
    ClientProvider clientProvider,
    List<RoleType> roleTypes,
    boolean isNewMember) {}
