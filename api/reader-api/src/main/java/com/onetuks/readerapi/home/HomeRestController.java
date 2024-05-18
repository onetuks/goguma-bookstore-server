package com.onetuks.readerapi.home;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeRestController {

  /**
   * 홈
   * - 로그인 가능한 url 반환
   * @return 200 OK
   */
  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HomeResponse> home() {
    return ResponseEntity.status(HttpStatus.OK).body(new HomeResponse());
  }

  @Getter
  public static class HomeResponse {

    private final String logoImgUrl;
    private final String kakaoAuthUrl;
    private final String naverAuthUrl;
    private final String googleAuthUrl;

    protected HomeResponse() {
      this.logoImgUrl = "test-logo-img-url.png";
      this.kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize";
      this.naverAuthUrl = "https://nid.naver.com/oauth2.0/authorize";
      this.googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth";
    }
  }
}
