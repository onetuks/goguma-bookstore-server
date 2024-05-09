package com.onetuks.moduleauth.oauth.dto;

import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import java.util.List;
import lombok.Builder;

@Builder
public record UserData(
    String name, String socialId, ClientProvider clientProvider, List<RoleType> roleTypes) {}
