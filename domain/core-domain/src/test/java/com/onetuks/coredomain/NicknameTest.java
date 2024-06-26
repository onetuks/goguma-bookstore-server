package com.onetuks.coredomain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.coredomain.member.model.vo.Nickname;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NicknameTest {

  @Test
  @DisplayName("설정할 수 없는 값으로 닉네임 객체 생성 시 예외를 던진다.")
  void createNickname_InvalidValue_ThrowException() {
    // Given
    String invalidValue = "admin";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> new Nickname(invalidValue));
  }

  @Test
  @DisplayName("특수문자를 포함해서 닉네임 객체 생성 시 예외를 던진다.")
  void createNickname_IncludeSpecialCharacter_ThrowException() {
    // Given
    String invalidValue = "!@#!@$%^&**";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> new Nickname(invalidValue));
  }
}
