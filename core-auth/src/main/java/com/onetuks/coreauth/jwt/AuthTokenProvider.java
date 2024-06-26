package com.onetuks.coreauth.jwt;

import static com.onetuks.coreauth.jwt.AuthToken.AUTHORITIES_KEY;
import static com.onetuks.coreauth.jwt.AuthToken.LOGIN_ID_KEY;

import com.onetuks.coreobj.enums.member.RoleType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenProvider {

  @Value("${jwt.issuer}")
  private String issuer;

  @Value("${jwt.accessTokenExpiryPeriod}")
  private long accessTokenExpiryPeriod;

  @Value("${jwt.refreshTokenExpiryPeriod}")
  private long refreshTokenExpiryPeriod;

  private final SecretKey secretKey;

  public AuthTokenProvider(@Value("${jwt.tokenSecretKey}") String secretKey) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  public AuthToken provideAccessToken(String socialId, Long loginId, List<RoleType> roleTypes) {
    return new AuthToken(
        createToken(socialId, loginId, roleTypes, getExpiryDate(accessTokenExpiryPeriod)),
        secretKey);
  }

  public AuthToken provideRefreshToken(String socialId, Long loginId, List<RoleType> roleTypes) {
    return new AuthToken(
        createToken(socialId, loginId, roleTypes, getExpiryDate(refreshTokenExpiryPeriod)),
        secretKey);
  }

  public AuthToken convertToAuthToken(String token) {
    return new AuthToken(token, secretKey);
  }

  private Date getExpiryDate(long expiryPeriod) {
    return new Date(System.currentTimeMillis() + expiryPeriod);
  }

  private String createToken(String socialId, Long loginId, List<RoleType> roleTypes, Date expiry) {
    return Jwts.builder()
        .subject(socialId)
        .claim(LOGIN_ID_KEY, loginId)
        .claim(AUTHORITIES_KEY, roleTypes.stream().map(RoleType::name).toArray(String[]::new))
        .signWith(secretKey, SIG.HS256)
        .expiration(expiry)
        .issuer(issuer)
        .issuedAt(new Date(System.currentTimeMillis()))
        .compact();
  }
}
