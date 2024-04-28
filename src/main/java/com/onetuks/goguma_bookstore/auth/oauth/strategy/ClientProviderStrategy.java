package com.onetuks.goguma_bookstore.auth.oauth.strategy;

import com.onetuks.modulepersistence.member.vo.UserData;

public interface ClientProviderStrategy {

  UserData getUserData(String accessToken);
}
