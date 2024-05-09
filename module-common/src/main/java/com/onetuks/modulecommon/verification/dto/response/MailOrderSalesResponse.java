package com.onetuks.modulecommon.verification.dto.response;

import java.util.List;

public record MailOrderSalesResponse(
    String resultCode,
    String resultMsg,
    String numOfRows,
    String pageNo,
    Integer totalCount,
    List<MailOrderSalesItemResponse> items) {

  public record MailOrderSalesItemResponse(
      Long opnSn,
      String prmmiYr,
      String prmmiMnno,
      String ctpvNm,
      String dclrInstNm,
      String operSttusCdNm,
      String bzmnNm,
      String crno,
      String brno,
      String lctnAddr,
      String dclrDate,
      String rnAddr,
      String prcsDeptDtlNm,
      String prcsDeptAreaNm,
      String prcsDeptNm,
      String chrgDeptTelno,
      String rprsvNm,
      String rprsvEmladr) {}
}
