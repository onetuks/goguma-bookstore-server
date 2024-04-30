package com.onetuks.moduleauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.moduleauth.AuthIntegrationTest;
import com.onetuks.moduleauth.service.dto.MemberCreateResult;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.member.vo.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberAuthServiceTest extends AuthIntegrationTest {

  @Autowired private MemberAuthService memberService;
  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("새로운 멤버를 생성한다.")
  void saveMemberIfNotExists_NotExistsMember_Test() {
    // Given
    UserData userData = MemberFixture.createUserData(RoleType.USER);

    // When
    MemberCreateResult result = memberService.saveMemberIfNotExists(userData);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isInstanceOf(Long.class).isNotNull(),
        () -> assertThat(result.name()).isEqualTo(userData.name()),
        () -> assertThat(result.socialId()).isEqualTo(userData.socialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(userData.clientProvider()),
        () -> assertThat(result.roleType()).isEqualTo(userData.roleType()),
        () -> assertThat(result.isNewMember()).isTrue());
  }

  @Test
  @DisplayName("이미 존재하는 멤버는 따로 생성하지 않고 해당 멤버를 반환한다.")
  void saveMemberIfNotExists_ExistsMember_Test() {
    // Given
    UserData userData = MemberFixture.createUserData(RoleType.USER);
    memberService.saveMemberIfNotExists(userData);

    // When
    MemberCreateResult result = memberService.saveMemberIfNotExists(userData);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isInstanceOf(Long.class).isNotNull(),
        () -> assertThat(result.name()).isEqualTo(userData.name()),
        () -> assertThat(result.socialId()).isEqualTo(userData.socialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(userData.clientProvider()),
        () -> assertThat(result.roleType()).isEqualTo(userData.roleType()),
        () -> assertThat(result.isNewMember()).isFalse());
  }

  @Test
  @DisplayName("회원탈퇴한다.")
  void deletMemberTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    String token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTc1MTk1Nzc5NTA1NTg4NDczMjYiLCJsb2dpbklkIjozLCJyb2xlIjoiVVNFUiIsImV4cCI6MTcxMzAzOTU4NiwiaXNzIjoiZ29ndW1hIiwiaWF0IjoxNzEyOTUzMTg2fQ.oIxlLpioIkXI_Qr32HMuABHXyLjZQqYAclORQ8RZ7AI";
    memberJpaRepository.flush();

    // When
    memberService.deleteMember(member.getMemberId(), token);

    // Then
    boolean result = memberJpaRepository.existsById(member.getMemberId());

    assertThat(result).isFalse();
  }
}