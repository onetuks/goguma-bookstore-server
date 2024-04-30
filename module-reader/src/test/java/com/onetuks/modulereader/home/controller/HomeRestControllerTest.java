package com.onetuks.modulereader.home.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulecommon.home.HomeRestController;
import com.onetuks.modulecommon.home.HomeRestController.HomeResponse;
import com.onetuks.modulereader.IntegrationTest;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class HomeRestControllerTest extends IntegrationTest {

  @Autowired private HomeRestController homeRestController;

  @Test
  void home() {
    // Given & When
    ResponseEntity<HomeResponse> result = homeRestController.home();

    // Then
    assertAll(
        "home-test",
        () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(result.getBody()).isNotNull(),
        () ->
            assertThat(Objects.requireNonNull(result.getBody()).getLogoImgUrl())
                .isEqualTo("test-logo-img-url.png"),
        () ->
            assertThat(Objects.requireNonNull(result.getBody()).getKakaoAuthUrl())
                .isEqualTo("https://kauth.kakao.com/oauth/authorize"),
        () ->
            assertThat(Objects.requireNonNull(result.getBody()).getNaverAuthUrl())
                .isEqualTo("https://nid.naver.com/oauth2.0/authorize"),
        () ->
            assertThat(Objects.requireNonNull(result.getBody()).getGoogleAuthUrl())
                .isEqualTo("https://accounts.google.com/o/oauth2/v2/auth"));
  }
}
