package com.onetuks.coreauth.jwt;

import com.onetuks.coreobj.enums.member.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
public class AuthToken {

  protected static final String AUTHORITIES_KEY = "roles";
  protected static final String LOGIN_ID_KEY = "loginId";

  @Getter private final String token;
  private final SecretKey secretKey;

  AuthToken(String token, SecretKey secretKey) {
    this.token = token;
    this.secretKey = secretKey;
  }

  public String getSocialId() {
    return getTokenClaims().getSubject();
  }

  public List<RoleType> getRoleTypes() {
    return Arrays.stream(getTokenClaims().get(AUTHORITIES_KEY, String[].class))
        .map(RoleType::valueOf)
        .toList();
  }

  public Authentication getAuthentication() {
    Claims claims = getTokenClaims();

    String socialId = claims.getSubject();
    Long loginId = claims.get(LOGIN_ID_KEY, Long.class);
    List<String> roles = Arrays.asList(claims.get(AUTHORITIES_KEY, String[].class));

    List<SimpleGrantedAuthority> authorities =
        roles.stream().map(SimpleGrantedAuthority::new).toList();

    CustomUserDetails customUserDetails =
        CustomUserDetails.builder()
            .socialId(socialId)
            .loginId(loginId)
            .authorities(authorities)
            .build();

    return new UsernamePasswordAuthenticationToken(customUserDetails, this, authorities);
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

  private Claims getTokenClaims() {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }
}
