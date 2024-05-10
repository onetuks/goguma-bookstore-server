package com.onetuks.modulereader.member.service.dto.result;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.ReaderIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberEntityInfoResultTest extends ReaderIntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("멤버 엔티티에서 멤버 정보 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.USER));

    // When
    MemberInfoResult result = MemberInfoResult.from(memberEntity);

    // Then
    assertAll(
        () -> assertThat(result.memberId()).isEqualTo(memberEntity.getMemberId()),
        () -> assertThat(result.nickname()).isEqualTo(memberEntity.getNickname()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(memberEntity.getProfileImgUrl()),
        () -> assertThat(result.defaultAddress()).isEqualTo(memberEntity.getDefaultAddress()),
        () -> assertThat(result.defaultAddressDetail()).isEqualTo(memberEntity.getDefaultAddressDetail()),
        () ->
            assertThat(result.defaultCashReceiptType())
                .isEqualTo(memberEntity.getDefaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReceiptNumber())
                .isEqualTo(memberEntity.getDefaultCashReceiptNumber()));
  }
}
