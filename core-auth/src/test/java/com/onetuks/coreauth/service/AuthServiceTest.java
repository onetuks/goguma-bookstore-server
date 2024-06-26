package com.onetuks.coreauth.service;

import static com.onetuks.coreobj.enums.member.RoleType.ADMIN;
import static com.onetuks.coreobj.enums.member.RoleType.AUTHOR;
import static com.onetuks.coreobj.enums.member.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.coreauth.CoreAuthIntegrationTest;
import com.onetuks.coreauth.jwt.AuthToken;
import com.onetuks.coreauth.service.dto.LogoutResult;
import com.onetuks.coreauth.service.dto.RefreshResult;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.readerdomain.member.service.MemberService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class AuthServiceTest extends CoreAuthIntegrationTest {

  @Autowired private AuthService authService;

  @MockBean private MemberService memberService;

  @Test
  @DisplayName("JWT 토큰을 저장한다.")
  void saveAccessTokenTest() {
    // Given
    String socialId = "socialId";
    Long loginId = 1L;
    List<RoleType> roleTypes = List.of(ADMIN, AUTHOR, USER);

    // When
    AuthToken authToken = authService.saveAccessToken(socialId, loginId, roleTypes);

    // Then
    assertThat(authToken).isNotNull();
  }

  @Test
  @DisplayName("JWT 토큰을 갱신한다.")
  void updateAccessTokenTest() {
    // Given
    AuthToken authToken = authService.saveAccessToken("socialId", 1L, List.of(ADMIN, AUTHOR, USER));
    Long loginId = 1L;
    List<RoleType> roleTypes = List.of(AUTHOR, USER);

    // When
    RefreshResult result = authService.updateAccessToken(authToken, loginId, roleTypes);

    // Then
    assertAll(
        () -> assertThat(result.accessToken()).isNotBlank(),
        () -> assertThat(result.loginId()).isEqualTo(loginId));
  }

  @Test
  @DisplayName("JWT 토큰을 삭제한다.")
  void logoutTest() {
    // Given
    AuthToken authToken = authService.saveAccessToken("socialId", 1L, List.of(ADMIN, AUTHOR, USER));

    // When
    LogoutResult result = authService.logout(authToken);

    // Then
    assertThat(result.islogout()).isTrue();
  }

  @Test
  @DisplayName("로그아웃하지 않았을때 로그아웃 여부를 확인한다.")
  void isLogout_NotLogOuted_Test() {
    // Given
    AuthToken authToken = authService.saveAccessToken("socialId", 1L, List.of(ADMIN, AUTHOR, USER));

    // When
    boolean result = authService.isLogout(authToken.getToken());

    // Then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("로그아웃 했을때 로그아웃 여부를 확인한다.")
  void isLogout_LogOuted_Test() {
    // Given
    AuthToken authToken = authService.saveAccessToken("socialId", 1L, List.of(ADMIN, AUTHOR, USER));
    authService.logout(authToken);

    // When
    boolean result = authService.isLogout(authToken.getToken());

    // Then
    assertThat(result).isTrue();
  }
}
