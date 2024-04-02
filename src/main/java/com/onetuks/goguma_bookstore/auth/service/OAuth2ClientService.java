package com.onetuks.goguma_bookstore.auth.service;

import com.onetuks.happyparkingserver.auth.jwt.AuthToken;
import com.onetuks.happyparkingserver.auth.model.Member;
import com.onetuks.happyparkingserver.auth.model.vo.ClientProvider;
import com.onetuks.happyparkingserver.auth.oauth.ClientProviderStrategyHandler;
import com.onetuks.happyparkingserver.auth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.happyparkingserver.auth.repository.MemberRepository;
import com.onetuks.happyparkingserver.auth.service.dto.LoginResult;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2ClientService {

  private final ClientProviderStrategyHandler clientProviderStrategyHandler;
  private final AuthService authService;
  private final MemberRepository memberRepository;

  public OAuth2ClientService(
      ClientProviderStrategyHandler clientProviderStrategyHandler,
      AuthService authService,
      MemberRepository memberRepository) {
    this.clientProviderStrategyHandler = clientProviderStrategyHandler;
    this.authService = authService;
    this.memberRepository = memberRepository;
  }

  @Transactional
  public LoginResult login(ClientProvider clientProvider, String accessToken) {
    ClientProviderStrategy clientProviderStrategy =
        clientProviderStrategyHandler.getClientStrategy(clientProvider);

    Member clientMember = clientProviderStrategy.getUserData(accessToken);
    String socialId = clientMember.getSocialId();
    ClientProvider oAuth2Provider = clientMember.getClientProvider();

    Optional<Member> optionalMember =
        memberRepository.findBySocialIdAndClientProvider(socialId, oAuth2Provider);
    Member savedMember = optionalMember.orElseGet(() -> memberRepository.save(clientMember));

    AuthToken newAuthToken = authService.saveAccessToken(savedMember.getId(), socialId);

    return LoginResult.of(
        newAuthToken.getToken(),
        optionalMember.isEmpty(),
        savedMember.getId(),
        savedMember.getNickname());
  }
}
