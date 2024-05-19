package com.onetuks.coreauth.service;

import com.onetuks.coreauth.jwt.AuthToken;
import com.onetuks.coreauth.oauth.ClientProviderStrategyHandler;
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
  public LoginResult login(ClientProvider clientProvider, String accessToken) {
    ClientProviderStrategy clientProviderStrategy =
        clientProviderStrategyHandler.getClientStrategy(clientProvider);

    AuthInfo clientMember = clientProviderStrategy.getAuthInfo(accessToken);

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
}
