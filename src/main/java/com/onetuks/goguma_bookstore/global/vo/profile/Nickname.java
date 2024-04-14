package com.onetuks.goguma_bookstore.global.vo.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Nickname {

  private static final List<String> INVALID_NICKNAMES = List.of("admin", "administrator", "root");
  private static final List<String> SPECIAL_CHARACTERS =
      List.of(
          "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", "[", "]", "{",
          "}", "|", "\\", ";", ":", "'", "\"", ",", "<", ">", ".", "/", "?");

  @Column(name = "nickname", unique = true)
  private String nicknameValue;

  public Nickname(String nicknameValue) {
    if (nicknameValue != null) {
      validateInvalidNickname(nicknameValue);
      validateSpecialCharacter(nicknameValue);
    }
    this.nicknameValue = nicknameValue;
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
