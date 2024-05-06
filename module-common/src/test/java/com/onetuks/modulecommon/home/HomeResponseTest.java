package com.onetuks.modulecommon.home;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulecommon.CommonIntegrationTest;
import com.onetuks.modulecommon.home.HomeRestController.HomeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HomeResponseTest extends CommonIntegrationTest {

  @Test
  @DisplayName("홈 접근 시 가능한 로그인 정보를 얻는다.")
  void homeResponseTest() {
    // Given & When
    HomeResponse result = new HomeResponse();

    // Then
    assertAll(
        () ->
            assertThat(result.getGoogleAuthUrl())
                .isEqualTo("https://accounts.google.com/o/oauth2/v2/auth"),
        () ->
            assertThat(result.getKakaoAuthUrl())
                .isEqualTo("https://kauth.kakao.com/oauth/authorize"),
        () -> assertThat(result.getLogoImgUrl()).isEqualTo("test-logo-img-url.png"),
        () ->
            assertThat(result.getNaverAuthUrl())
                .isEqualTo("https://nid.naver.com/oauth2.0/authorize"));
  }
}
