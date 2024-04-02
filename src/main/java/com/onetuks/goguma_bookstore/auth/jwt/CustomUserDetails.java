package com.onetuks.goguma_bookstore.auth.jwt;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

  private final Long memberId;
  private final String socialId;
  private final List<? extends GrantedAuthority> authorities;

  @Builder
  public CustomUserDetails(
      Long memberId, String socialId, List<? extends GrantedAuthority> authorities) {
    this.memberId = memberId;
    this.socialId = socialId;
    this.authorities = authorities;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (CustomUserDetails) obj;
    return Objects.equals(this.memberId, that.memberId)
        && Objects.equals(this.socialId, that.socialId)
        && Objects.equals(this.authorities, that.authorities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberId, socialId, authorities);
  }

  @Override
  public String toString() {
    return "CustomUserDetails["
        + "memberId="
        + memberId
        + ", "
        + "socialId="
        + socialId
        + ", "
        + "authorities="
        + authorities
        + ']';
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return socialId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
