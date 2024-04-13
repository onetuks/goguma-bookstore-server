package com.onetuks.goguma_bookstore.member.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberEntryInfoResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberEntryInfoResponseTest extends IntegrationTest {

  @Test
  @DisplayName("회원가입 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberEntryInfoResult entryInfoResult = new MemberEntryInfoResult(1L, "nickname", true);

    // When
    MemberEntryInfoResponse result = MemberEntryInfoResponse.from(entryInfoResult);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(entryInfoResult.memberId()),
        () -> assertThat(result.nickname()).isEqualTo(entryInfoResult.nickname()),
        () -> assertThat(result.alarmPermission()).isEqualTo(entryInfoResult.alarmPermission()));
  }
}