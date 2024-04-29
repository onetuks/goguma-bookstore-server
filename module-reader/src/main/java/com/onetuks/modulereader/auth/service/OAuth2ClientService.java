package com.onetuks.modulereader.auth.service;

import com.onetuks.modulereader.auth.jwt.AuthToken;
import com.onetuks.modulereader.auth.oauth.ClientProviderStrategyHandler;
import com.onetuks.modulereader.auth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.modulereader.auth.service.dto.LoginResult;
import com.onetuks.modulereader.member.service.MemberService;
import com.onetuks.modulereader.member.service.dto.result.MemberCreateResult;
import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.member.vo.UserData;
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

    UserData clientMember = clientProviderStrategy.getUserData(accessToken);
    String socialId = clientMember.socialId();

    MemberCreateResult savedMember = memberService.saveMemberIfNotExists(clientMember);

    AuthToken newAuthToken = authService.saveAccessToken(socialId, savedMember.memberId());

    return LoginResult.of(
        newAuthToken.getToken(),
        savedMember.isNewMember(),
        savedMember.memberId(),
        savedMember.name());
  }
}
