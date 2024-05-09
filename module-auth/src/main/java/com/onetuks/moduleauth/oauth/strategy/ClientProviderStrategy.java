package com.onetuks.moduleauth.oauth.strategy;

import com.onetuks.moduleauth.oauth.dto.UserData;

public interface ClientProviderStrategy {

  UserData getUserData(String accessToken);
}
