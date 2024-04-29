package com.onetuks.modulereader.auth.jwt;

import java.util.Optional;

public interface AuthTokenRepository {

  void save(String accessToken, String refreshToken);

  void delete(String accessToken);

  Optional<String> findRefreshToken(String accessToken);
}
