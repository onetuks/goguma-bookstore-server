package com.onetuks.goguma_bookstore.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
public class AuthToken {

  protected static final String AUTHORITIES_KEY = "role";
  protected static final String LOGIN_ID_KEY = "loginId";

  @Getter private final String token;
  private final Key key;

  AuthToken(String token, Key key) {
    this.token = token;
    this.key = key;
  }

  AuthToken(String socialId, Long loginId, String role, Date expiryDate, Key key) {
    this.token = createAccessToken(socialId, loginId, role, expiryDate, key);
    this.key = key;
  }

  AuthToken(Date expiryDate, Key key) {
    this.token = createRefreshToken(expiryDate, key);
    this.key = key;
  }

  public Authentication getAuthentication() {
    Claims claims = getTokenClaims();

    String[] roles = {claims.get(AUTHORITIES_KEY).toString()};
    int loginId = (int) claims.get(LOGIN_ID_KEY);

    List<SimpleGrantedAuthority> authorities =
        Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();

    CustomUserDetails customUserDetails =
        CustomUserDetails.builder()
            .socialId(claims.getSubject())
            .loginId((long) loginId)
            .authorities(authorities)
            .build();

    return new UsernamePasswordAuthenticationToken(customUserDetails, this, authorities);
  }

  public Claims getTokenClaims() {
    JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();

    return jwtParser.parseClaimsJws(token).getBody();
  }

  public boolean isValidTokenClaims() {
    Optional<Object> claims = Optional.empty();
    try {
      claims = Optional.ofNullable(getTokenClaims());
    } catch (SecurityException e) {
      log.info("Invalid JWT signature.");
    } catch (MalformedJwtException e) {
      log.info("Invalid JWT token.");
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token.");
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token.");
    } catch (IllegalArgumentException e) {
      log.info("JWT token compact of handler are invalid.");
    } catch (Exception e) {
      return false;
    }
    return claims.isPresent();
  }

  private String createAccessToken(
      String socialId, Long loginId, String role, Date expiry, Key key) {
    return Jwts.builder()
        .setSubject(socialId)
        .claim(LOGIN_ID_KEY, loginId)
        .claim(AUTHORITIES_KEY, role)
        .signWith(key, SignatureAlgorithm.HS256)
        .setExpiration(expiry)
        .compact();
  }

  private String createRefreshToken(Date expiry, Key key) {
    Claims claims = Jwts.claims();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }
}
