package com.onetuks.modulereader.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultAddressEditResult;
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
