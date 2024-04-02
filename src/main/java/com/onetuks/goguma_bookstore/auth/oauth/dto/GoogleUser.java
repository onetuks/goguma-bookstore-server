package com.onetuks.goguma_bookstore.auth.oauth.dto;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@JsonNaming(SnakeCaseStrategy.class)
public class GoogleUser {

  private String sub;
  private String email;
  private String name;

  public GoogleUser(String sub, String email, String name) {
    this.sub = sub;
    this.email = email;
    this.name = name;
  }
}
