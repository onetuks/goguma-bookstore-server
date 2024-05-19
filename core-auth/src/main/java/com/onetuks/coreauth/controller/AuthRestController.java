package com.onetuks.coreauth.controller;

import static com.onetuks.coreauth.jwt.AuthHeaderUtil.HEADER_AUTHORIZATION;
import static com.onetuks.coreobj.enums.member.ClientProvider.GOOGLE;
import static com.onetuks.coreobj.enums.member.ClientProvider.KAKAO;
import static com.onetuks.coreobj.enums.member.ClientProvider.NAVER;

import com.onetuks.coreauth.controller.dto.LoginResponse;
import com.onetuks.coreauth.controller.dto.LogoutResponse;
import com.onetuks.coreauth.controller.dto.RefreshResponse;
import com.onetuks.coreauth.jwt.AuthHeaderUtil;
import com.onetuks.coreauth.jwt.AuthToken;
import com.onetuks.coreauth.jwt.AuthTokenProvider;
import com.onetuks.coreauth.service.AuthService;
import com.onetuks.coreauth.service.OAuth2ClientService;
import com.onetuks.coreauth.service.dto.LoginResult;
import com.onetuks.coreauth.service.dto.LogoutResult;
import com.onetuks.coreauth.service.dto.RefreshResult;
import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.readerdomain.member.service.MemberService;
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
  private final MemberService memberService;

  public AuthRestController(
      AuthTokenProvider authTokenProvider,
      OAuth2ClientService oAuth2ClientService,
      AuthService authService,
      MemberService memberService) {
    this.authTokenProvider = authTokenProvider;
    this.oAuth2ClientService = oAuth2ClientService;
    this.authService = authService;
    this.memberService = memberService;
  }

  @PostMapping(path = "/kakao")
  public ResponseEntity<LoginResponse> kakaoLogin(HttpServletRequest request) {
    LoginResult loginResult =
        oAuth2ClientService.login(KAKAO, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(loginResult));
  }

  @PostMapping(path = "/google")
  public ResponseEntity<LoginResponse> googleLogin(HttpServletRequest request) {
    LoginResult loginResult =
        oAuth2ClientService.login(GOOGLE, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(loginResult));
  }

  @PostMapping(path = "/naver")
  public ResponseEntity<LoginResponse> naverLogin(HttpServletRequest request) {
    LoginResult loginResult =
        oAuth2ClientService.login(NAVER, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(loginResult));
  }

  @PutMapping(path = "/refresh")
  public ResponseEntity<RefreshResponse> refreshToken(
      HttpServletRequest request, @MemberId Long loginId) {
    AuthToken authToken = getAuthToken(request);

    RefreshResult authRefreshResult =
        authService.updateAccessToken(authToken, loginId, authToken.getRoleTypes());

    return ResponseEntity.status(HttpStatus.OK).body(RefreshResponse.from(authRefreshResult));
  }

  @DeleteMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
    AuthToken authToken = getAuthToken(request);

    LogoutResult logoutResult = authService.logout(authToken);

    return ResponseEntity.status(HttpStatus.OK).body(LogoutResponse.from(logoutResult));
  }

  @DeleteMapping("/withdraw")
  public ResponseEntity<Void> withdrawMember(HttpServletRequest request, @MemberId Long memberId) {
    AuthToken authToken = getAuthToken(request);

    authService.logout(authToken);
    memberService.deleteMember(memberId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private AuthToken getAuthToken(HttpServletRequest request) {
    String accessToken = AuthHeaderUtil.extractAuthToken(request);
    return authTokenProvider.convertToAuthToken(accessToken);
  }
}
