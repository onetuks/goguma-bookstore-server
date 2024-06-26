package com.onetuks.coreauth.oauth.strategy;

import com.onetuks.coreauth.config.KakaoClientConfig;
import com.onetuks.coreauth.exception.TokenValidFailedException;
import com.onetuks.coreauth.oauth.dto.KakaoAuthToken;
import com.onetuks.coreauth.oauth.dto.KakaoUser;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.error.ErrorCode;
import com.onetuks.coreweb.config.WebClientConfig;
import java.util.List;
import java.util.Objects;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class KakaoClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;
  private final KakaoClientConfig kakaoClientConfig;

  public KakaoClientProviderStrategy(WebClient webClient, KakaoClientConfig kakaoClientConfig) {
    this.webClient = webClient;
    this.kakaoClientConfig = kakaoClientConfig;
  }

  @Override
  public AuthInfo getAuthInfo(String authToken) {
    KakaoUser kakaoUser =
        webClient
            .get()
            .uri(
                kakaoClientConfig
                    .kakaoClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUri())
            .headers(httpHeaders -> httpHeaders.set("Authorization", authToken))
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
        .roles(List.of(RoleType.USER))
        .build();
  }

  @Override
  public KakaoAuthToken getOAuth2Token(String authCode) {
    return webClient
        .post()
        .uri(kakaoClientConfig.kakaoClientRegistration().getProviderDetails().getTokenUri())
        .headers(
            httpHeaders ->
                httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"))
        .body(BodyInserters.fromFormData(buildFormData(authCode)))
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
        .bodyToMono(KakaoAuthToken.class)
        .block();
  }

  private MultiValueMap<String, String> buildFormData(String authToken) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("client_id", kakaoClientConfig.getClientId());
    formData.add("client_secret", kakaoClientConfig.getClientSecret());
    formData.add("redirect_uri", kakaoClientConfig.kakaoClientRegistration().getRedirectUri());
    formData.add("code", authToken);
    return formData;
  }
}
