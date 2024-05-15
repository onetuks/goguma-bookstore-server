package com.onetuks.scmdomain.verification.webclient.dto.response;

import java.util.List;

public record BusinessNumberResponse(
    String status_code,
    Integer match_cnt,
    Integer request_cnt,
    List<BusinessNumberDataResponse> data) {

  public record BusinessNumberDataResponse(
      String b_no,
      String b_stt,
      String b_stt_cd,
      String tax_type,
      String tax_type_cd,
      String end_dt,
      String utcc_yn,
      String tax_type_change_dt,
      String invoice_apply_dt,
      String rbf_tax_type,
      String rbf_tax_type_cd) {}
}
