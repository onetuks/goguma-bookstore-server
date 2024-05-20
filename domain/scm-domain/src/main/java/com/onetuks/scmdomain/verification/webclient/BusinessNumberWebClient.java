package com.onetuks.scmdomain.verification.webclient;

import com.onetuks.coreobj.error.ErrorCode;
import com.onetuks.coreweb.config.WebClientConfig;
import com.onetuks.scmdomain.verification.webclient.dto.request.BusinessNumberRequest;
import com.onetuks.scmdomain.verification.webclient.dto.response.BusinessNumberResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class BusinessNumberWebClient {

  private static final String BUSINESS_NUMBER_URL =
      "https://api.odcloud.kr/api/nts-businessman/v1/status";

  @Value("${openapi.data-go-kr.secret-key}")
  private String secretKey;

  private final WebClient webClient;
  private final URIBuilder uriBuilder;

  public BusinessNumberWebClient(WebClient webClient, URIBuilder uriBuilder) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
  }

  public BusinessNumberResponse requestData(String businessNumber) {
    BusinessNumberRequest request = new BusinessNumberRequest(List.of(businessNumber));

    return webClient
        .post()
        .uri(uriBuilder.buildUri(BUSINESS_NUMBER_URL, buildMultiValueMap()))
        .bodyValue(request)
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
        .bodyToMono(BusinessNumberResponse.class)
        .block();
  }

  private MultiValueMap<String, String> buildMultiValueMap() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("serviceKey", secretKey);
    return params;
  }
}
