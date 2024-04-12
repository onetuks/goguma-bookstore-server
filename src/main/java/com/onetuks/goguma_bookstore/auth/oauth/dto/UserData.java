package com.onetuks.goguma_bookstore.auth.oauth.dto;

import com.onetuks.goguma_bookstore.member.vo.ClientProvider;
import com.onetuks.goguma_bookstore.member.vo.RoleType;
import lombok.Builder;

@Builder
public record UserData(
    String name, String socialId, ClientProvider clientProvider, RoleType roleType) {}
