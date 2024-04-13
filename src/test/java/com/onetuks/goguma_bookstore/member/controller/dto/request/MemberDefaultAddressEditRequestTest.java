package com.onetuks.goguma_bookstore.member.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberDefaultAddressEditParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberDefaultAddressEditRequestTest extends IntegrationTest {

  @Test
  @DisplayName("멤버 기본 배송지 수정 요청 객체에서 파람 객체로 변환한다.")
  void toTest() {
    // Given
    MemberDefaultAddressEditRequest request =
        new MemberDefaultAddressEditRequest("강원도 춘천시 중앙로", "대 빠니 생가");

    // When
    MemberDefaultAddressEditParam result = request.to();

    // Then
    assertAll(
        () -> assertThat(result.defaultAddress()).isEqualTo(request.defaultAddress()),
        () -> assertThat(result.defaultAddressDetail()).isEqualTo(request.defaultAddressDetail()));
  }
}
