package com.onetuks.coreauth.oauth.strategy;

public interface ClientProviderStrategy {

  AuthInfo getUserData(String accessToken);
}
