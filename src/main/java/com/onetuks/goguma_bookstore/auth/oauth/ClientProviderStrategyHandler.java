package com.onetuks.goguma_bookstore.auth.oauth;

import com.onetuks.goguma_bookstore.auth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.goguma_bookstore.auth.oauth.strategy.GoogleClientProviderStrategy;
import com.onetuks.goguma_bookstore.auth.oauth.strategy.KakaoClientProviderStrategy;
import com.onetuks.goguma_bookstore.auth.oauth.strategy.NaverClientProviderStrategy;
import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class ClientProviderStrategyHandler {

  private final Map<ClientProvider, ClientProviderStrategy> strategies = new ConcurrentHashMap<>();

  public ClientProviderStrategyHandler(
      KakaoClientProviderStrategy kakaoClientStrategy,
      GoogleClientProviderStrategy googleClientStrategy,
      NaverClientProviderStrategy naverClientStrategy) {
    this.strategies.put(ClientProvider.KAKAO, kakaoClientStrategy);
    this.strategies.put(ClientProvider.GOOGLE, googleClientStrategy);
    this.strategies.put(ClientProvider.NAVER, naverClientStrategy);
  }

  public ClientProviderStrategy getClientStrategy(ClientProvider provider) {
    return strategies.get(provider);
  }
}
