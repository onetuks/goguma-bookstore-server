package com.onetuks.modulereader.member.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulereader.ReaderIntegrationTest;
import org.junit.jupiter.api.Test;

class MemberEntityDefaultAddressEditResultTest extends ReaderIntegrationTest {

  @Test
  void fromTest() {
    // Given
    MemberEntity memberEntity = MemberFixture.create(RoleType.USER);

    // When
    MemberDefaultAddressEditResult result = MemberDefaultAddressEditResult.from(memberEntity);

    // Then
    assertAll(
        () -> assertThat(result.defaultAddress()).isEqualTo(memberEntity.getDefaultAddress()),
        () ->
            assertThat(result.defaultAddressDetail()).isEqualTo(memberEntity.getDefaultAddressDetail()));
  }
}
