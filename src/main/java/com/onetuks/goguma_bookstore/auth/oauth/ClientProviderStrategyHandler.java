package com.onetuks.goguma_bookstore.auth.oauth;

import com.onetuks.goguma_bookstore.auth.model.vo.ClientProvider;
import com.onetuks.goguma_bookstore.auth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.goguma_bookstore.auth.oauth.strategy.GoogleClientProviderStrategy;
import com.onetuks.goguma_bookstore.auth.oauth.strategy.KakaoClientProviderStrategy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class ClientProviderStrategyHandler {

  private final Map<ClientProvider, ClientProviderStrategy> strateties = new ConcurrentHashMap<>();

  public ClientProviderStrategyHandler(
      KakaoClientProviderStrategy kakaoClientStrategy,
      GoogleClientProviderStrategy googleClientStrategy) {
    this.strateties.put(ClientProvider.KAKAO, kakaoClientStrategy);
    this.strateties.put(ClientProvider.GOOGLE, googleClientStrategy);
  }

  public ClientProviderStrategy getClientStrategy(ClientProvider provider) {
    return strateties.get(provider);
  }
}
