package com.onetuks.modulepersistence.member.vo;

import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import lombok.Builder;

@Builder
public record UserData(
    String name, String socialId, ClientProvider clientProvider, RoleType roleType) {}
