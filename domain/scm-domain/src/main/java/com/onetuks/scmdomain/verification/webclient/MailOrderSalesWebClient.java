package com.onetuks.scmdomain.verification.webclient;

import com.onetuks.coreobj.error.ErrorCode;
import com.onetuks.coreweb.config.URIBuilder;
import com.onetuks.coreweb.config.WebClientConfig;
import com.onetuks.scmdomain.verification.webclient.dto.response.MailOrderSalesResponse;
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
public class MailOrderSalesWebClient {

  private static final String MAIL_ORDER_SALES_URL =
      "https://apis.data.go.kr/1130000/MllBs_1Service/getMllBsBiznoInfo_1";

  @Value("${openapi.data-go-kr.secret-key}")
  private String secretKey;

  private final WebClient webClient;
  private final URIBuilder uriBuilder;

  public MailOrderSalesWebClient(WebClient webClient, URIBuilder uriBuilder) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
  }

  public MailOrderSalesResponse requestData(String businessNumber) {
    return webClient
        .get()
        .uri(uriBuilder.buildUri(MAIL_ORDER_SALES_URL, buildMultiValueMap(businessNumber)))
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
