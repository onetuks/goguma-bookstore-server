package com.onetuks.goguma_bookstore.auth.oauth.strategy;

import static com.onetuks.modulepersistence.global.error.ErrorCode.OAUTH_CLIENT_SERVER_ERROR;
import static com.onetuks.modulepersistence.global.error.ErrorCode.UNAUTHORIZED_TOKEN;

import com.onetuks.goguma_bookstore.auth.exception.TokenValidFailedException;
import com.onetuks.goguma_bookstore.auth.oauth.dto.KakaoUser;
import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.vo.UserData;
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
  public UserData getUserData(String accessToken) {
    KakaoUser kakaoUser =
        webClient
            .get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .headers(httpHeaders -> httpHeaders.set("Authorization", accessToken))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> Mono.error(new TokenValidFailedException(UNAUTHORIZED_TOKEN)))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(KakaoUser.class)
            .block();

    Objects.requireNonNull(kakaoUser);

    return UserData.builder()
        .name(kakaoUser.getProperties().getNickname())
        .socialId(String.valueOf(kakaoUser.getId()))
        .clientProvider(ClientProvider.KAKAO)
        .roleType(RoleType.USER)
        .build();
  }
}
