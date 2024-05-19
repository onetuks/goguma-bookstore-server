package com.onetuks.coreauth.oauth.strategy;

import com.onetuks.coredomain.member.model.vo.AuthInfo;

public interface ClientProviderStrategy {

  AuthInfo getAuthInfo(String accessToken);
}
