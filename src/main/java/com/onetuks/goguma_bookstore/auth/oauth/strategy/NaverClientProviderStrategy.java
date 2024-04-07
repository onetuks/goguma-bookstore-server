package com.onetuks.goguma_bookstore.auth.oauth.strategy;

import static com.onetuks.goguma_bookstore.global.error.ErrorCode.OAUTH_CLIENT_SERVER_ERROR;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.UNAUTHORIZED_TOKEN;

import com.onetuks.goguma_bookstore.auth.exception.TokenValidFailedException;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.oauth.dto.NaverUser;
import com.onetuks.goguma_bookstore.auth.vo.ClientProvider;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import java.util.Objects;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NaverClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;

  public NaverClientProviderStrategy(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public Member getUserData(String accessToken) {
    NaverUser naverUser =
        webClient
            .get()
            .uri("https://openapi.naver.com/v1/nid/me")
            .headers(httpHeaders -> httpHeaders.set("Authorization", accessToken))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> Mono.error(new TokenValidFailedException(UNAUTHORIZED_TOKEN)))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(NaverUser.class)
            .block();

    Objects.requireNonNull(naverUser);
    Objects.requireNonNull(naverUser.getResponse());

    return Member.builder()
        .name(naverUser.getResponse().getName())
        .socialId(naverUser.getResponse().getId())
        .clientProvider(ClientProvider.NAVER)
        .roleType(RoleType.USER)
        .build();
  }
}
