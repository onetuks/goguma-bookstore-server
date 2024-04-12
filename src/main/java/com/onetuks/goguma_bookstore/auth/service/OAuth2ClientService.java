package com.onetuks.goguma_bookstore.auth.service;

import com.onetuks.goguma_bookstore.auth.jwt.AuthToken;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.oauth.ClientProviderStrategyHandler;
import com.onetuks.goguma_bookstore.auth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.goguma_bookstore.auth.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.auth.service.dto.LoginResult;
import com.onetuks.goguma_bookstore.auth.vo.ClientProvider;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2ClientService {

  private final ClientProviderStrategyHandler clientProviderStrategyHandler;
  private final AuthService authService;
  private final MemberJpaRepository memberJpaRepository;

  public OAuth2ClientService(
      ClientProviderStrategyHandler clientProviderStrategyHandler,
      AuthService authService,
      MemberJpaRepository memberJpaRepository) {
    this.clientProviderStrategyHandler = clientProviderStrategyHandler;
    this.authService = authService;
    this.memberJpaRepository = memberJpaRepository;
  }

  @Transactional
  public LoginResult login(ClientProvider clientProvider, String accessToken) {
    ClientProviderStrategy clientProviderStrategy =
        clientProviderStrategyHandler.getClientStrategy(clientProvider);

    Member clientMember = clientProviderStrategy.getUserData(accessToken);
    String socialId = clientMember.getSocialId();
    ClientProvider oAuth2Provider = clientMember.getClientProvider();

    Optional<Member> optionalMember =
        memberJpaRepository.findBySocialIdAndClientProvider(socialId, oAuth2Provider);
    Member savedMember = optionalMember.orElseGet(() -> memberJpaRepository.save(clientMember));

    AuthToken newAuthToken = authService.saveAccessToken(savedMember.getMemberId(), socialId);

    return LoginResult.of(
        newAuthToken.getToken(),
        optionalMember.isEmpty(),
        savedMember.getMemberId(),
        savedMember.getName());
  }
}
