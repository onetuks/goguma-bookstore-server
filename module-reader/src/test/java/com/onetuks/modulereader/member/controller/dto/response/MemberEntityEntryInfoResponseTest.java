package com.onetuks.modulereader.member.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulereader.ReaderIntegrationTest;
import com.onetuks.modulereader.member.service.dto.result.MemberEntryInfoResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberEntityEntryInfoResponseTest extends ReaderIntegrationTest {

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
