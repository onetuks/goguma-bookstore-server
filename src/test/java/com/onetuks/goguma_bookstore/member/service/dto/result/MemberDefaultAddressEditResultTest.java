package com.onetuks.goguma_bookstore.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import org.junit.jupiter.api.Test;

class MemberDefaultAddressEditResultTest extends IntegrationTest {

  @Test
  void fromTest() {
    // Given
    Member member = MemberFixture.create(RoleType.USER);

    // When
    MemberDefaultAddressEditResult result = MemberDefaultAddressEditResult.from(member);

    // Then
    assertAll(
        () -> assertThat(result.defaultAddress()).isEqualTo(member.getDefaultAddress()),
        () ->
            assertThat(result.defaultAddressDetail()).isEqualTo(member.getDefaultAddressDetail()));
  }
}
