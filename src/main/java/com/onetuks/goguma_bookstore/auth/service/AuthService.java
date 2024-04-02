package com.onetuks.goguma_bookstore.auth.service;

import static com.onetuks.happyparkingserver.global.error.ErrorCode.EXPIRED_REFRESH_TOKEN;

import com.onetuks.happyparkingserver.auth.exception.TokenExpiredException;
import com.onetuks.happyparkingserver.auth.jwt.AuthToken;
import com.onetuks.happyparkingserver.auth.jwt.AuthTokenCacheRepository;
import com.onetuks.happyparkingserver.auth.jwt.AuthTokenProvider;
import com.onetuks.happyparkingserver.auth.service.dto.LogoutResult;
import com.onetuks.happyparkingserver.auth.service.dto.RefreshResult;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final AuthTokenProvider authTokenProvider;
  private final AuthTokenCacheRepository authTokenCacheRepository;

  public AuthService(
      AuthTokenProvider authTokenProvider, AuthTokenCacheRepository refreshTokenCacheRepository) {
    this.authTokenProvider = authTokenProvider;
    this.authTokenCacheRepository = refreshTokenCacheRepository;
  }

  @Transactional
  public AuthToken saveAccessToken(Long memberId, String socialId) {
    AuthToken accessToken = authTokenProvider.provideAccessToken(socialId, memberId);
    AuthToken refreshToken = authTokenProvider.provideRefreshToken();

    authTokenCacheRepository.save(accessToken.getToken(), refreshToken.getToken());

    return accessToken;
  }

  @Transactional
  public RefreshResult updateAccessToken(AuthToken accessToken, Long memberId) {
    Claims claims = accessToken.getTokenClaims();
    String socialId = claims.getSubject();

    validateRefreshToken(accessToken.getToken());

    authTokenCacheRepository.delete(accessToken.getToken());
    AuthToken newAccessToken = saveAccessToken(memberId, socialId);

    return RefreshResult.of(newAccessToken.getToken(), memberId);
  }

  @Transactional
  public LogoutResult logout(AuthToken authToken) {
    authTokenCacheRepository.delete(authToken.getToken());
    return new LogoutResult(true);
  }

  @Transactional(readOnly = true)
  public boolean isLogout(String accessToken) {
    return authTokenCacheRepository.findRefreshToken(accessToken).isEmpty();
  }

  private void validateRefreshToken(String accessToken) {
    boolean isValidRefreshToken = findRefreshToken(accessToken).isValidTokenClaims();

    if (!isValidRefreshToken) {
      throw new TokenExpiredException(EXPIRED_REFRESH_TOKEN);
    }
  }

  private AuthToken findRefreshToken(String accessToken) {
    String refreshToken =
        authTokenCacheRepository
            .findRefreshToken(accessToken)
            .orElseThrow(() -> new TokenExpiredException(EXPIRED_REFRESH_TOKEN));

    return authTokenProvider.convertToAuthToken(refreshToken);
  }
}
