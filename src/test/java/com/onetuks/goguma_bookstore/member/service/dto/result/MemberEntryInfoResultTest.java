package com.onetuks.goguma_bookstore.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberEntryInfoResultTest extends IntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("회원 엔티티에서 회원가입 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));

    // When
    MemberEntryInfoResult result = MemberEntryInfoResult.from(member);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(member.getMemberId()),
        () -> assertThat(result.nickname()).isEqualTo(member.getNickname()),
        () -> assertThat(result.alarmPermission()).isEqualTo(member.getAlarmPermission()));
  }
}
