package com.onetuks.moduleauth.oauth.strategy;

import com.onetuks.modulepersistence.member.vo.UserData;

public interface ClientProviderStrategy {

  UserData getUserData(String accessToken);
}