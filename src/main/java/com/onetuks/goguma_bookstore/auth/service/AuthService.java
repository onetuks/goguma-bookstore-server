package com.onetuks.goguma_bookstore.auth.service;

import static com.onetuks.goguma_bookstore.global.error.ErrorCode.EXPIRED_REFRESH_TOKEN;

import com.onetuks.goguma_bookstore.auth.exception.TokenExpiredException;
import com.onetuks.goguma_bookstore.auth.jwt.AuthToken;
import com.onetuks.goguma_bookstore.auth.jwt.AuthTokenProvider;
import com.onetuks.goguma_bookstore.auth.jwt.AuthTokenRepository;
import com.onetuks.goguma_bookstore.auth.service.dto.LogoutResult;
import com.onetuks.goguma_bookstore.auth.service.dto.RefreshResult;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final AuthTokenProvider authTokenProvider;
  private final AuthTokenRepository authTokenRepository;

  public AuthService(AuthTokenProvider authTokenProvider, AuthTokenRepository authTokenRepository) {
    this.authTokenProvider = authTokenProvider;
    this.authTokenRepository = authTokenRepository;
  }

  @Transactional
  public AuthToken saveAccessToken(Long memberId, String socialId) {
    AuthToken accessToken = authTokenProvider.provideAccessToken(socialId, memberId);
    AuthToken refreshToken = authTokenProvider.provideRefreshToken();

    authTokenRepository.save(accessToken.getToken(), refreshToken.getToken());

    return accessToken;
  }

  @Transactional
  public RefreshResult updateAccessToken(AuthToken accessToken, Long loginId) {
    Claims claims = accessToken.getTokenClaims();
    String socialId = claims.getSubject();

    validateRefreshToken(accessToken.getToken());

    authTokenRepository.delete(accessToken.getToken());
    AuthToken newAccessToken = saveAccessToken(loginId, socialId);

    return RefreshResult.of(newAccessToken.getToken(), loginId);
  }

  @Transactional
  public LogoutResult logout(AuthToken authToken) {
    authTokenRepository.delete(authToken.getToken());
    return new LogoutResult(true);
  }

  @Transactional(readOnly = true)
  public boolean isLogout(String accessToken) {
    return authTokenRepository.findRefreshToken(accessToken).isEmpty();
  }

  private void validateRefreshToken(String accessToken) {
    boolean isValidRefreshToken = findRefreshToken(accessToken).isValidTokenClaims();

    if (!isValidRefreshToken) {
      throw new TokenExpiredException(EXPIRED_REFRESH_TOKEN);
    }
  }

  private AuthToken findRefreshToken(String accessToken) {
    String refreshToken =
        authTokenRepository
            .findRefreshToken(accessToken)
            .orElseThrow(() -> new TokenExpiredException(EXPIRED_REFRESH_TOKEN));

    return authTokenProvider.convertToAuthToken(refreshToken);
  }
}
