package com.onetuks.modulecommon.verification;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.modulecommon.CommonIntegrationTest;
import com.onetuks.modulecommon.verification.dto.response.MailOrderSalesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MailOrderSalesWebClientServiceTest extends CommonIntegrationTest {

  @Autowired private MailOrderSalesWebClientService service;

  @Test
  @DisplayName("유효한 사업자등록번호를 이용해서 통신판매신고정보를 조회한다.")
  void requestDataTest() {
    // Given
    String businessNumber = "6615900628";

    // When
    MailOrderSalesResponse result = service.requestData(businessNumber);

    // Then
    assertThat(result.resultCode()).isEqualTo("00");
    assertThat(result.items())
        .isNotEmpty()
        .allSatisfy(
            item -> {
              assertThat(item.brno()).isEqualTo(businessNumber);
              assertThat(item.opnSn()).isPositive();
              assertThat(item.operSttusCdNm()).isEqualTo("정상영업");
            });
  }
}
