package com.onetuks.scmdomain.verification.webclient;

import com.onetuks.scmdomain.verification.webclient.dto.result.RegistrationIsbnResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class IsbnWebClient {

  private static final String ISBN_URL = "https://www.nl.go.kr/seoji/SearchApi.do";

  @Value("${openapi.center-lib.secret-key}")
  private String secretKey;

  private final WebClient webClient;
  private final URIBuilder uriBuilder;

  public IsbnWebClient(WebClient webClient, URIBuilder uriBuilder) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
  }

  public RegistrationIsbnResult requestData(String isbn) {
    return webClient
        .post()
        .uri(uriBuilder.buildUri(ISBN_URL, buildMultiValueMap(isbn)))
        .retrieve()
        .onStatus(
            HttpStatusCode::isError,
            response ->
                Mono.error(
                    new WebClientResponseException(
                        ErrorCode.OPENAPI_REQUEST_ERROR.getMessage(),
                        response.statusCode().value(),
                        response.statusCode().toString(),
                        response.headers().asHttpHeaders(),
                        null,
                        null,
                        response.request())))
        .bodyToMono(RegistrationIsbnResult.class)
        .block();
  }

  private MultiValueMap<String, String> buildMultiValueMap(String isbn) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("cert_key", secretKey);
    params.add("result_style", "json");
    params.add("page_no", "1");
    params.add("page_size", "10");
    params.add("isbn", isbn);
    return params;
  }
}
