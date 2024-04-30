package com.onetuks.modulereader.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.member.vo.AuthInfo;
import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberCreateResultTest extends IntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("멤버 엔티티에서 멤버 생성 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Member savedMember = memberJpaRepository.save(MemberFixture.create(RoleType.USER));

    // When
    MemberCreateResult result = MemberCreateResult.from(savedMember, true);

    // Then
    AuthInfo authInfo = savedMember.getAuthInfo();

    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(savedMember.getMemberId()),
        () -> assertThat(result.name()).isEqualTo(authInfo.getName()),
        () -> assertThat(result.socialId()).isEqualTo(authInfo.getSocialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(authInfo.getClientProvider()),
        () -> assertThat(result.roleType()).isEqualTo(authInfo.getRoleType()),
        () -> assertThat(result.isNewMember()).isTrue());
  }
}
