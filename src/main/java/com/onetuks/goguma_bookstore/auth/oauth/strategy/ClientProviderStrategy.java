package com.onetuks.goguma_bookstore.auth.oauth.strategy;

import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;

public interface ClientProviderStrategy {

  UserData getUserData(String accessToken);
}
