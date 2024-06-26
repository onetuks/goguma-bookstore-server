package com.onetuks.coreauth.service;

import com.onetuks.coreauth.jwt.AuthHeaderUtil;
import com.onetuks.coreauth.jwt.AuthToken;
import com.onetuks.coreauth.oauth.ClientProviderStrategyHandler;
import com.onetuks.coreauth.oauth.dto.KakaoAuthToken;
import com.onetuks.coreauth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.coreauth.service.dto.LoginResult;
import com.onetuks.coredomain.member.dto.MemberAuthResult;
import com.onetuks.coredomain.member.model.vo.AuthInfo;
import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.readerdomain.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2ClientService {

  private final ClientProviderStrategyHandler clientProviderStrategyHandler;
  private final AuthService authService;
  private final MemberService memberService;

  public OAuth2ClientService(
      ClientProviderStrategyHandler clientProviderStrategyHandler,
      AuthService authService,
      MemberService memberService) {
    this.clientProviderStrategyHandler = clientProviderStrategyHandler;
    this.authService = authService;
    this.memberService = memberService;
  }

  @Transactional
  public LoginResult loginWithAuthToken(ClientProvider clientProvider, String authToken) {
    ClientProviderStrategy clientProviderStrategy =
        clientProviderStrategyHandler.getClientStrategy(clientProvider);

    AuthInfo clientMember = clientProviderStrategy.getAuthInfo(authToken);

    MemberAuthResult savedMember = memberService.createMemberIfNotExists(clientMember);

    AuthToken newAuthToken =
        authService.saveAccessToken(
            clientMember.socialId(), savedMember.memberId(), savedMember.roleTypes());

    return LoginResult.of(
        newAuthToken.getToken(),
        savedMember.isNewMember(),
        savedMember.memberId(),
        savedMember.name(),
        savedMember.roleTypes());
  }

  @Transactional
  public LoginResult loginWithAuthCode(ClientProvider clientProvider, String authCode) {
    ClientProviderStrategy clientProviderStrategy =
        clientProviderStrategyHandler.getClientStrategy(clientProvider);

    String coreCode = authCode.replace("Bearer ", "");
    KakaoAuthToken authToken = clientProviderStrategy.getOAuth2Token(coreCode);

    return loginWithAuthToken(
        clientProvider, AuthHeaderUtil.TOKEN_PREFIX + authToken.access_token());
  }
}
