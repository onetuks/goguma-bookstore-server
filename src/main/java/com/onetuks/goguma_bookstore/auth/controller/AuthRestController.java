package com.onetuks.goguma_bookstore.auth.controller;

import static com.onetuks.goguma_bookstore.auth.jwt.AuthHeaderUtil.HEADER_AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

import com.onetuks.goguma_bookstore.auth.controller.dto.LoginResponse;
import com.onetuks.goguma_bookstore.auth.controller.dto.LogoutResponse;
import com.onetuks.goguma_bookstore.auth.controller.dto.RefreshResponse;
import com.onetuks.goguma_bookstore.auth.jwt.AuthHeaderUtil;
import com.onetuks.goguma_bookstore.auth.jwt.AuthToken;
import com.onetuks.goguma_bookstore.auth.jwt.AuthTokenProvider;
import com.onetuks.goguma_bookstore.auth.model.vo.ClientProvider;
import com.onetuks.goguma_bookstore.auth.service.AuthService;
import com.onetuks.goguma_bookstore.auth.service.OAuth2ClientService;
import com.onetuks.goguma_bookstore.auth.service.dto.LoginResult;
import com.onetuks.goguma_bookstore.auth.service.dto.LogoutResult;
import com.onetuks.goguma_bookstore.auth.service.dto.RefreshResult;
import com.onetuks.goguma_bookstore.auth.util.LoginId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/auth")
public class AuthRestController {

  private final AuthTokenProvider authTokenProvider;
  private final OAuth2ClientService oAuth2ClientService;
  private final AuthService authService;

  public AuthRestController(
      AuthTokenProvider authTokenProvider,
      OAuth2ClientService oAuth2ClientService,
      AuthService authService) {
    this.authTokenProvider = authTokenProvider;
    this.oAuth2ClientService = oAuth2ClientService;
    this.authService = authService;
  }

  @PostMapping(path = "/kakao")
  public ResponseEntity<LoginResponse> kakaoLogin(HttpServletRequest request) {
    LoginResult loginResult =
        oAuth2ClientService.login(ClientProvider.KAKAO, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(OK).body(LoginResponse.from(loginResult));
  }

  @PostMapping(path = "/google")
  public ResponseEntity<LoginResponse> googleLogin(HttpServletRequest request) {
    LoginResult loginResult =
        oAuth2ClientService.login(ClientProvider.GOOGLE, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(OK).body(LoginResponse.from(loginResult));
  }

  @PostMapping(path = "/naver")
  public ResponseEntity<LoginResponse> naverLogin(HttpServletRequest request) {
    LoginResult loginResult =
        oAuth2ClientService.login(ClientProvider.NAVER, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(OK).body(LoginResponse.from(loginResult));
  }

  @PutMapping(path = "/refresh")
  public ResponseEntity<RefreshResponse> refreshToken(
      HttpServletRequest request, @LoginId Long loginId) {
    String accessToken = AuthHeaderUtil.extractAuthToken(request);
    AuthToken authToken = authTokenProvider.convertToAuthToken(accessToken);

    RefreshResult authRefreshResult = authService.updateAccessToken(authToken, loginId);

    return ResponseEntity.status(HttpStatus.OK).body(RefreshResponse.from(authRefreshResult));
  }

  @DeleteMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
    String appToken = AuthHeaderUtil.extractAuthToken(request);
    AuthToken authToken = authTokenProvider.convertToAuthToken(appToken);

    LogoutResult logoutResult = authService.logout(authToken);

    return ResponseEntity.status(HttpStatus.OK).body(LogoutResponse.from(logoutResult));
  }
}
