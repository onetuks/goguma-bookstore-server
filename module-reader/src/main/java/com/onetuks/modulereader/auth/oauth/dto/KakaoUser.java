package com.onetuks.modulereader.auth.oauth.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@JsonNaming(SnakeCaseStrategy.class)
public class KakaoUser {

  private Long id;
  private Properties properties;
  private KakaoAccount kakaoAccount;

  public KakaoUser(Long id, Properties properties, KakaoAccount kakaoAccount) {
    this.id = id;
    this.properties = properties;
    this.kakaoAccount = kakaoAccount;
  }

  @Getter
  @NoArgsConstructor(access = PROTECTED)
  @JsonNaming(SnakeCaseStrategy.class)
  public static class Properties {

    private String nickname;

    protected Properties(String nickname) {
      this.nickname = nickname;
    }
  }

  @Getter
  @NoArgsConstructor(access = PROTECTED)
  public static class KakaoAccount {

    private String email;

    protected KakaoAccount(String email) {
      this.email = email;
    }
  }
}
