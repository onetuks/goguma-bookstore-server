package com.onetuks.goguma_bookstore.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.member.vo.ClientProvider;
import com.onetuks.goguma_bookstore.member.vo.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberCreateResultTest extends IntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("멤버 엔티티에서 멤버 생성 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Member savedMember =
        memberJpaRepository.save(
            Member.builder()
                .name("이병건")
                .socialId("01012")
                .clientProvider(ClientProvider.KAKAO)
                .roleType(RoleType.USER)
                .build());

    // When
    MemberCreateResult result = MemberCreateResult.from(savedMember, true);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(savedMember.getMemberId()),
        () -> assertThat(result.name()).isEqualTo(savedMember.getName()),
        () -> assertThat(result.socialId()).isEqualTo(savedMember.getSocialId()),
        () -> assertThat(result.clientProvider()).isEqualTo(savedMember.getClientProvider()),
        () -> assertThat(result.roleType()).isEqualTo(savedMember.getRoleType()),
        () -> assertThat(result.isNewMember()).isTrue());
  }
}
