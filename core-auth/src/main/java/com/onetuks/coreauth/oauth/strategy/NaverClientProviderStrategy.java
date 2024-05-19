package com.onetuks.coreauth.oauth.strategy;

import com.onetuks.coreauth.exception.TokenValidFailedException;
import com.onetuks.coreauth.oauth.dto.NaverUser;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.error.ErrorCode;
import java.util.List;
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
  public AuthInfo getAuthInfo(String accessToken) {
    NaverUser naverUser =
        webClient
            .get()
            .uri("https://openapi.naver.com/v1/nid/me")
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
            .bodyToMono(NaverUser.class)
            .block();

    Objects.requireNonNull(naverUser);
    Objects.requireNonNull(naverUser.getResponse());

    return AuthInfo.builder()
        .name(naverUser.getResponse().getName())
        .socialId(naverUser.getResponse().getId())
        .clientProvider(ClientProvider.NAVER)
        .roles(List.of(RoleType.USER))
        .build();
  }
}
