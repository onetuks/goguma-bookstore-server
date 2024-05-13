package com.onetuks.coreauth.oauth.strategy;

import com.onetuks.coreauth.exception.TokenValidFailedException;
import com.onetuks.coreauth.oauth.dto.KakaoUser;
import com.onetuks.coreobj.error.ErrorCode;
import com.onetuks.dbstorage.global.vo.auth.ClientProvider;
import com.onetuks.dbstorage.global.vo.auth.RoleType;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;

  public KakaoClientProviderStrategy(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public AuthInfo getUserData(String accessToken) {
    KakaoUser kakaoUser =
        webClient
            .get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .headers(httpHeaders -> httpHeaders.set("Authorization", accessToken))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(KakaoUser.class)
            .block();

    Objects.requireNonNull(kakaoUser);

    return AuthInfo.builder()
        .name(kakaoUser.getProperties().getNickname())
        .socialId(String.valueOf(kakaoUser.getId()))
        .clientProvider(ClientProvider.KAKAO)
        .roleTypes(List.of(RoleType.USER))
        .build();
  }
}