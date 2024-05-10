package com.onetuks.modulereader.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.ReaderIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberEntityEntryInfoResultTest extends ReaderIntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("회원 엔티티에서 회원가입 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.USER));

    // When
    MemberEntryInfoResult result = MemberEntryInfoResult.from(memberEntity);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(memberEntity.getMemberId()),
        () -> assertThat(result.nickname()).isEqualTo(memberEntity.getNickname()),
        () -> assertThat(result.alarmPermission()).isEqualTo(memberEntity.getAlarmPermission()));
  }
}
