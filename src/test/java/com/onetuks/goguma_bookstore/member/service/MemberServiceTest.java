package com.onetuks.goguma_bookstore.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.oauth.dto.UserData;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.member.service.dto.result.MemberCreateResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends IntegrationTest {

  @Autowired private MemberService memberService;

  @Test
  @DisplayName("새로운 멤버를 생성한다.")
  void saveMemberIfNotExists_NotExistsMember_Test() {
    // Given
    UserData userData = MemberFixture.createUserData();

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
    UserData userData = MemberFixture.createUserData();
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
}
