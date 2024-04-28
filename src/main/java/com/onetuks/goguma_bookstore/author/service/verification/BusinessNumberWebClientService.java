package com.onetuks.goguma_bookstore.author.service.verification;

import static com.onetuks.modulepersistence.global.error.ErrorCode.OPENAPI_REQUEST_ERROR;

import com.onetuks.goguma_bookstore.author.service.verification.dto.request.BusinessNumberRequest;
import com.onetuks.goguma_bookstore.author.service.verification.dto.response.BusinessNumberResponse;
import com.onetuks.goguma_bookstore.global.service.URIBuilder;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class BusinessNumberWebClientService {

  private static final String BUSINESS_NUMBER_URL =
      "https://api.odcloud.kr/api/nts-businessman/v1/status";

  @Value("${openapi.data-go-kr.secret-key}")
  private String secretKey;

  private final WebClient webClient;
  private final URIBuilder uriBuilder;

  public BusinessNumberWebClientService(WebClient webClient, URIBuilder uriBuilder) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
  }

  protected BusinessNumberResponse requestData(String businessNumber) {
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
                        OPENAPI_REQUEST_ERROR.getMessage(),
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
