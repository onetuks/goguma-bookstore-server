package com.onetuks.moduleauth.service;

import com.onetuks.moduleauth.jwt.AuthToken;
import com.onetuks.moduleauth.oauth.ClientProviderStrategyHandler;
import com.onetuks.moduleauth.oauth.dto.UserData;
import com.onetuks.moduleauth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.moduleauth.service.dto.LoginResult;
import com.onetuks.moduleauth.service.dto.MemberCreateResult;
import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2ClientService {

  private final ClientProviderStrategyHandler clientProviderStrategyHandler;
  private final AuthService authService;
  private final MemberAuthService memberAuthService;

  public OAuth2ClientService(
      ClientProviderStrategyHandler clientProviderStrategyHandler,
      AuthService authService,
      MemberAuthService memberAuthService) {
    this.clientProviderStrategyHandler = clientProviderStrategyHandler;
    this.authService = authService;
    this.memberAuthService = memberAuthService;
  }

  @Transactional
  public LoginResult login(ClientProvider clientProvider, String accessToken) {
    ClientProviderStrategy clientProviderStrategy =
        clientProviderStrategyHandler.getClientStrategy(clientProvider);

    UserData clientMember = clientProviderStrategy.getUserData(accessToken);

    MemberCreateResult savedMember = memberAuthService.saveMemberIfNotExists(clientMember);

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
