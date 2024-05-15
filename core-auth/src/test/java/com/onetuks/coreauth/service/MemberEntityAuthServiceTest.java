package com.onetuks.coreauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.coreauth.AuthIntegrationTest;
import com.onetuks.coreauth.service.dto.MemberCreateResult;
import com.onetuks.dbstorage.fixture.MemberFixture;
import com.onetuks.dbstorage.global.vo.auth.ClientProvider;
import com.onetuks.dbstorage.global.vo.auth.RoleType;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.repository.MemberJpaRepository;
import com.onetuks.filestorage.util.UUIDProvider;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberEntityAuthServiceTest extends AuthIntegrationTest {

  @Autowired private MemberAuthService memberService;
  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("새로운 멤버를 생성한다.")
  void saveMemberIfNotExists_NotExistsMember_Test() {
    // Given
    AuthInfo authInfo = createUserData();

    // When
    MemberCreateResult result = memberService.saveMemberIfNotExists(authInfo);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isInstanceOf(Long.class).isNotNull(),
        () -> assertThat(result.name()).isEqualTo(authInfo.name()),
        () -> assertThat(result.socialId()).isEqualTo(authInfo.socialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(authInfo.clientProvider()),
        () -> assertThat(result.roleTypes()).isEqualTo(authInfo.roles()),
        () -> assertThat(result.isNewMember()).isTrue());
  }

  @Test
  @DisplayName("이미 존재하는 멤버는 따로 생성하지 않고 해당 멤버를 반환한다.")
  void saveMemberIfNotExists_ExistsMember_Test() {
    // Given
    AuthInfo authInfo = createUserData();
    memberService.saveMemberIfNotExists(authInfo);

    // When
    MemberCreateResult result = memberService.saveMemberIfNotExists(authInfo);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isInstanceOf(Long.class).isNotNull(),
        () -> assertThat(result.name()).isEqualTo(authInfo.name()),
        () -> assertThat(result.socialId()).isEqualTo(authInfo.socialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(authInfo.clientProvider()),
        () -> assertThat(result.roleTypes()).isEqualTo(authInfo.roles()),
        () -> assertThat(result.isNewMember()).isFalse());
  }

  @Test
  @DisplayName("회원탈퇴한다.")
  void deletMemberTest() {
    // Given
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    memberJpaRepository.flush();

    // When
    memberService.deleteMember(memberEntity.getMemberId());

    // Then
    boolean result = memberJpaRepository.existsById(memberEntity.getMemberId());

    assertThat(result).isFalse();
  }

  private static AuthInfo createUserData() {
    return AuthInfo.builder()
        .name("빠니보틀" + UUIDProvider.getUUID())
        .socialId(UUIDProvider.getUUID())
        .clientProvider(ClientProvider.NAVER)
        .roleTypes(List.of(RoleType.USER))
        .build();
  }
}
