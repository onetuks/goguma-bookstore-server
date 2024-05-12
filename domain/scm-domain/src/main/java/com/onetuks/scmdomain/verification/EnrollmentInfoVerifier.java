package com.onetuks.scmdomain.verification;

import com.onetuks.scmdomain.verification.webclient.BusinessNumberWebClient;
import com.onetuks.scmdomain.verification.webclient.MailOrderSalesWebClient;
import com.onetuks.scmdomain.verification.webclient.dto.response.BusinessNumberResponse;
import com.onetuks.scmdomain.verification.webclient.dto.response.MailOrderSalesResponse;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentInfoVerifier {

  private final BusinessNumberWebClient businessNumberWebClient;
  private final MailOrderSalesWebClient mailOrderSalesWebClient;

  public EnrollmentInfoVerifier(
      BusinessNumberWebClient businessNumberWebClient,
      MailOrderSalesWebClient mailOrderSalesWebClient) {
    this.businessNumberWebClient = businessNumberWebClient;
    this.mailOrderSalesWebClient = mailOrderSalesWebClient;
  }

  public void verifyEnrollmentInfo(String businessNumber, String mailOrderSalesNumber) {
    BusinessNumberResponse businessNumberResponse =
        businessNumberWebClient.requestData(businessNumber);
    MailOrderSalesResponse mailOrderSalesResponse =
        mailOrderSalesWebClient.requestData(businessNumber);

    verifyBusinessData(businessNumber, businessNumberResponse);
    verifyMailOrderSalesData(businessNumber, mailOrderSalesNumber, mailOrderSalesResponse);
  }

  private void verifyMailOrderSalesData(
      String businessNumber, String mailOrderSalesNumber, MailOrderSalesResponse response) {
    boolean isMailOrderSalesNumberMatched =
        Objects.requireNonNull(response).items().stream()
            .filter(item -> Objects.equals(item.operSttusCdNm(), "정상영업"))
            .filter(item -> Objects.equals(String.valueOf(item.opnSn()), mailOrderSalesNumber))
            .anyMatch(item -> Objects.equals(item.brno(), businessNumber));

    if (!isMailOrderSalesNumberMatched) {
      throw new IllegalArgumentException("해당 통신판매신고번호가 존재하지 않습니다.");
    }
  }

  private void verifyBusinessData(String businessNumber, BusinessNumberResponse response) {
    boolean isbusinessNumberMatched =
        Objects.requireNonNull(response).data().stream()
            .filter(data -> Objects.equals(data.b_stt_cd(), "01"))
            .anyMatch(data -> Objects.equals(data.b_no(), businessNumber));

    if (!isbusinessNumberMatched) {
      throw new IllegalArgumentException("해당 사업자등록번호가 존재하지 않습니다.");
    }
  }
}
