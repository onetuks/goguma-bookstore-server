package com.onetuks.coredomain.member.model.vo;

import java.util.List;

public record Nickname(
    String nicknameValue
) {

  private static final List<String> INVALID_NICKNAMES = List.of("admin", "administrator", "root");
  private static final List<String> SPECIAL_CHARACTERS =
      List.of(
          "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", "[", "]", "{",
          "}", "|", "\\", ";", ":", "'", "\"", ",", "<", ">", ".", "/", "?");

  public Nickname {
    if (nicknameValue != null) {
      validateInvalidNickname(nicknameValue);
      validateSpecialCharacter(nicknameValue);
    }
  }

  private void validateInvalidNickname(String nickname) {
    boolean isInvalidNickname = INVALID_NICKNAMES.stream().anyMatch(nickname::contains);

    if (isInvalidNickname) {
      throw new IllegalArgumentException("닉네임으로 사용할 수 없는 값입니다. : " + nickname);
    }
  }

  private void validateSpecialCharacter(String nickname) {
    boolean hasSpecialCharacter = SPECIAL_CHARACTERS.stream().anyMatch(nickname::contains);

    if (hasSpecialCharacter) {
      throw new IllegalArgumentException("특수문자는 사용할 수 없습니다. : " + nickname);
    }
  }
}
