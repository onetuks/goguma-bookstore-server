package com.onetuks.goguma_bookstore.member.controller.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberEntryInfoParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberEntryInfoRequestTest extends IntegrationTest {

  @Test
  @DisplayName("회원가입 정보 요청 객체를 파람 객체로 변환한다.")
  void toTest() {
    // Given
    MemberEntryInfoRequest request = new MemberEntryInfoRequest("nickname", true);

    // When
    MemberEntryInfoParam result = request.to();

    // Then
    assertAll(
        () -> assertThat(result.nickname()).isEqualTo(request.nickname()),
        () -> assertThat(result.alarmPermission()).isTrue());
  }
}
