package com.onetuks.modulereader.author.controller.dto.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.util.RandomValueProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.author.service.dto.result.AuthorEnrollmentDetailsResult;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorEnrollmentDetailsResponseTest extends IntegrationTest {

  @Test
  @DisplayName("작가 입점 정보 요청 결과 객체에서 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    long memberId = 412_123L;
    AuthorEnrollmentDetailsResult detailsResult =
        new AuthorEnrollmentDetailsResult(
            123_412L,
            memberId,
            List.of(RoleType.USER, RoleType.AUTHOR),
            FileWrapperFixture.createFile(memberId, FileType.PROFILES).getUri(),
            "빠니보틀",
            "소개글",
            "https://www.instagram.com/panibottle" + memberId,
            RandomValueProvider.createBusinessNumber(),
            RandomValueProvider.createMailOrderSalesNumber(),
            true,
            LocalDateTime.now());

    // When
    AuthorEnrollmentDetailsResponse result = AuthorEnrollmentDetailsResponse.from(detailsResult);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(detailsResult.authorId()),
        () -> assertThat(result.memberId()).isEqualTo(detailsResult.memberId()),
        () -> assertThat(result.roleTypes()).contains(RoleType.USER, RoleType.AUTHOR),
        () -> assertThat(result.profileImgUrl()).isEqualTo(detailsResult.profileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(detailsResult.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(detailsResult.introduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(detailsResult.instagramUrl()),
        () -> assertThat(result.businessNumber()).isEqualTo(detailsResult.businessNumber()),
        () -> assertThat(result.enrollmentPassed()).isEqualTo(detailsResult.enrollmentPassed()),
        () -> assertThat(result.enrollmentAt()).isEqualTo(detailsResult.enrollmentAt()));
  }
}
