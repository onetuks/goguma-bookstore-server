package com.onetuks.goguma_bookstore.auth.oauth.strategy;

import static com.onetuks.goguma_bookstore.global.error.ErrorCode.OAUTH_CLIENT_SERVER_ERROR;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.UNAUTHORIZED_TOKEN;

import com.onetuks.goguma_bookstore.auth.exception.TokenValidFailedException;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.model.vo.ClientProvider;
import com.onetuks.goguma_bookstore.auth.model.vo.RoleType;
import com.onetuks.goguma_bookstore.auth.oauth.dto.GoogleUser;
import java.util.Objects;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GoogleClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;

  public GoogleClientProviderStrategy(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public Member getUserData(String accessToken) {
    GoogleUser googleUser =
        webClient
            .get()
            .uri("https://www.googleapis.com/oauth2/v3/userinfo")
            .headers(httpHeaders -> httpHeaders.set("Authorization", accessToken))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> Mono.error(new TokenValidFailedException(UNAUTHORIZED_TOKEN)))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(GoogleUser.class)
            .block();

    Objects.requireNonNull(googleUser);

    return Member.builder()
        .name(googleUser.getName())
        .socialId(googleUser.getSub())
        .clientProvider(ClientProvider.GOOGLE)
        .roleType(RoleType.USER)
        .build();
  }
}
