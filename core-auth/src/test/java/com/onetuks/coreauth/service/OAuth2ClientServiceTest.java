package com.onetuks.coreauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.onetuks.coreauth.AuthIntegrationTest;
import com.onetuks.coreauth.oauth.strategy.GoogleClientProviderStrategy;
import com.onetuks.coreauth.service.dto.LoginResult;
import com.onetuks.dbstorage.global.vo.auth.ClientProvider;
import com.onetuks.dbstorage.global.vo.auth.RoleType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class OAuth2ClientServiceTest extends AuthIntegrationTest {

  @Autowired private OAuth2ClientService oAuth2ClientService;
  @MockBean private GoogleClientProviderStrategy googleClientProviderStrategy;

  @Test
  @DisplayName("구글 소셜 로그인 클라이언트를 활용해 로그인한다.")
  void login_GoogleClient_Test() {
    // Given
    ClientProvider clientProvider = ClientProvider.GOOGLE;
    given(googleClientProviderStrategy.getUserData(anyString()))
        .willReturn(
            AuthInfo.builder()
                .socialId("googleId")
                .roleTypes(List.of(RoleType.USER))
                .name("googleName")
                .clientProvider(clientProvider)
                .build());

    // When
    LoginResult result = oAuth2ClientService.login(clientProvider, "googleAccessToken");

    // Then
    assertAll(
        () -> assertThat(result.loginId()).isPositive(),
        () -> assertThat(result.name()).isEqualTo("googleName"),
        () -> assertThat(result.roleTypes()).containsExactly(RoleType.USER),
        () -> assertThat(result.isNewMember()).isTrue(),
        () -> assertThat(result.accessToken()).isInstanceOf(String.class));
  }
}
