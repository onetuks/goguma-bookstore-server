package com.onetuks.modulereader.author.service.verification;

import static com.onetuks.modulepersistence.global.error.ErrorCode.OPENAPI_REQUEST_ERROR;

import com.onetuks.modulereader.author.service.verification.dto.response.MailOrderSalesResponse;
import com.onetuks.modulereader.global.service.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class MailOrderSalesWebClientService {

  private static final String MAIL_ORDER_SALES_URL =
      "https://apis.data.go.kr/1130000/MllBs_1Service/getMllBsBiznoInfo_1";

  @Value("${openapi.data-go-kr.secret-key}")
  private String secretKey;

  private final WebClient webClient;
  private final URIBuilder uriBuilder;

  public MailOrderSalesWebClientService(WebClient webClient, URIBuilder uriBuilder) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
  }

  protected MailOrderSalesResponse requestData(String businessNumber) {
    return webClient
        .get()
        .uri(uriBuilder.buildUri(MAIL_ORDER_SALES_URL, buildMultiValueMap(businessNumber)))
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
        .bodyToMono(MailOrderSalesResponse.class)
        .block();
  }

  private MultiValueMap<String, String> buildMultiValueMap(String businessNumber) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("serviceKey", secretKey);
    params.add("pageNo", "1");
    params.add("numOfRows", "100");
    params.add("resultType", "json");
    params.add("brno", businessNumber);
    return params;
  }
}
