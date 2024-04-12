package com.onetuks.goguma_bookstore.auth.service;

import com.onetuks.goguma_bookstore.auth.jwt.AuthToken;
import com.onetuks.goguma_bookstore.auth.oauth.ClientProviderStrategyHandler;
import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.auth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.goguma_bookstore.auth.service.dto.LoginResult;
import com.onetuks.goguma_bookstore.member.service.MemberService;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberCreateResult;
import com.onetuks.goguma_bookstore.member.vo.ClientProvider;
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

    AuthToken newAuthToken = authService.saveAccessToken(savedMember.memberId(), socialId);

    return LoginResult.of(
        newAuthToken.getToken(),
        savedMember.isNewMember(),
        savedMember.memberId(),
        savedMember.name());
  }
}
