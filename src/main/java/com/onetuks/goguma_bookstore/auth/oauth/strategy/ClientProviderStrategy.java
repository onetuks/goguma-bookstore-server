package com.onetuks.goguma_bookstore.auth.oauth.strategy;

import com.onetuks.goguma_bookstore.auth.model.Member;

public interface ClientProviderStrategy {

  Member getUserData(String accessToken);
}
