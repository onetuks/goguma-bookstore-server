package com.onetuks.modulereader.member.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultAddressEditResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberDefaultAddressEditResponseTest extends IntegrationTest {

  @Test
  @DisplayName("멤버 기본 배송지 정보 수정 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberDefaultAddressEditResult addressResult =
        new MemberDefaultAddressEditResult("강원도 춘천시 중앙로 133", "유튜브 대통령 빠니보틀 생가");

    // When
    MemberDefaultAddressEditResponse result = MemberDefaultAddressEditResponse.from(addressResult);

    // Then
    assertAll(
        () -> assertThat(result.defaultAddress()).isEqualTo(addressResult.defaultAddress()),
        () ->
            assertThat(result.defaultAddressDetail())
                .isEqualTo(addressResult.defaultAddressDetail()));
  }
}
