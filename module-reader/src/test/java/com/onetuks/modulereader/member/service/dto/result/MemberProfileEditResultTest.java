package com.onetuks.modulereader.member.service.dto.result;

import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.member.service.dto.result.MemberProfileEditResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberProfileEditResultTest extends IntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("멤버 엔티티에서 멤버 프로필 수정 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Member save = memberJpaRepository.save(MemberFixture.create(RoleType.USER));

    // When
    MemberProfileEditResult result = MemberProfileEditResult.from(save);

    // Then
    assertAll(
        () -> assertEquals(save.getMemberId(), result.memberId()),
        () -> assertEquals(save.getNickname(), result.nickname()),
        () -> assertEquals(save.getProfileImgUrl(), result.profileImgUrl()),
        () -> assertEquals(save.getAlarmPermission(), result.alarmPermission()),
        () -> assertEquals(save.getDefaultAddress(), result.defaultAddress()),
        () -> assertEquals(save.getDefaultAddressDetail(), result.defaultAddressDetail()),
        () -> assertEquals(save.getDefaultCashReceiptType(), result.defaultCashReceiptType()),
        () -> assertEquals(save.getDefaultCashReceiptNumber(), result.defaultCashReceiptNumber()));
  }
}
