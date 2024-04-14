package com.onetuks.goguma_bookstore.member.service.dto.result;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberInfoResultTest extends IntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("멤버 엔티티에서 멤버 정보 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));

    // When
    MemberInfoResult result = MemberInfoResult.from(member);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(member.getMemberId()),
        () -> assertThat(result.nickname()).isEqualTo(member.getNickname()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(member.getProfileImgUrl()),
        () -> assertThat(result.defaultAddress()).isEqualTo(member.getDefaultAddress()),
        () -> assertThat(result.defaultAddressDetail()).isEqualTo(member.getDefaultAddressDetail()),
        () ->
            assertThat(result.defaultCashReceiptType())
                .isEqualTo(member.getDefaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(member.getDefaultCashReceiptNumber()));
  }
}
