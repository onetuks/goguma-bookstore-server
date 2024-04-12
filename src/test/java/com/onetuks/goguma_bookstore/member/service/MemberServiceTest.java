package com.onetuks.goguma_bookstore.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.member.service.dto.param.MemberEntryInfoParam;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberCreateResult;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberEntryInfoResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class MemberServiceTest extends IntegrationTest {

  @Autowired private MemberService memberService;
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
  @DisplayName("회원가입 정보를 업데이트한다.")
  void entryMemberInfo_Test() {
    // Given
    UserData userData = MemberFixture.createUserData(RoleType.USER);
    MemberCreateResult savedMember = memberService.saveMemberIfNotExists(userData);
    MemberEntryInfoParam param = new MemberEntryInfoParam("빠니보틀니", true);

    // When
    MemberEntryInfoResult result = memberService.entryMemberInfo(savedMember.memberId(), param);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(savedMember.memberId()),
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.alarmPermission()).isEqualTo(param.alarmPermission()));
  }

  @Test
  @DisplayName("중복된 닉네임이 있는 경우 예외를 던진다.")
  void entryMemberInfo_DuplicatedNickname_ExceptionTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    memberJpaRepository.flush();

    UserData userData = MemberFixture.createUserData(RoleType.USER);
    MemberCreateResult createResult = memberService.saveMemberIfNotExists(userData);
    MemberEntryInfoParam param = new MemberEntryInfoParam(member.getNickname(), true);

    // When & Then
    assertThatThrownBy(
            () -> {
              memberService.entryMemberInfo(createResult.memberId(), param);
              memberJpaRepository.flush();
            })
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}
