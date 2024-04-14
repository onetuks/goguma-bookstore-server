package com.onetuks.goguma_bookstore.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
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
