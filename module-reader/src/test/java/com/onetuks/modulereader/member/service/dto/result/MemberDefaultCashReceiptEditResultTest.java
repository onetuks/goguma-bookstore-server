package com.onetuks.modulereader.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultCashReceiptEditResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberDefaultCashReceiptEditResultTest extends IntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("멤버 엔티티에서 기본 현금영수증 수정 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    Member save = memberJpaRepository.save(MemberFixture.create(RoleType.USER));

    // When
    MemberDefaultCashReceiptEditResult result = MemberDefaultCashReceiptEditResult.from(save);

    // Then
    assertAll(
        () ->
            assertThat(result.defaultCashReceiptType()).isEqualTo(save.getDefaultCashReceiptType()),
        () ->
            assertThat(result.defaultCashReciptNumber())
                .isEqualTo(save.getDefaultCashReceiptNumber()));
  }
}
