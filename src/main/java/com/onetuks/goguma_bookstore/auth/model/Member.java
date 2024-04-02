package com.onetuks.goguma_bookstore.auth.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.onetuks.happyparkingserver.auth.model.vo.ClientProvider;
import com.onetuks.happyparkingserver.auth.model.vo.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(
    name = "members",
    uniqueConstraints = @UniqueConstraint(columnNames = {"social_id", "client_provider"}))
public class Member {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Column(name = "social_id", nullable = false)
  private String socialId;

  @Enumerated(value = STRING)
  @Column(name = "client_provider", nullable = false)
  private ClientProvider clientProvider;

  @Enumerated(value = STRING)
  @Column(name = "role", nullable = false)
  private RoleType roleType;

  private Member(
      String nickname, String socialId, ClientProvider clientProvider, RoleType roleType) {
    this.nickname = nickname;
    this.socialId = socialId;
    this.clientProvider = clientProvider;
    this.roleType = roleType;
  }

  public static Member of(
      String nickname, String socialId, ClientProvider clientProvider, RoleType roleType) {
    return new Member(nickname, socialId, clientProvider, roleType);
  }
}
