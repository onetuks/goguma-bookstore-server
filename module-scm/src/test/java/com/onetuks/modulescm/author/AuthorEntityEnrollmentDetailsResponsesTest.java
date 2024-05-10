package com.onetuks.modulescm.author;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.modulecommon.util.RandomValueProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulescm.ScmIntegrationTest;
import com.onetuks.modulescm.author.controller.dto.response.AuthorEnrollmentDetailsResponse.AuthorEnrollmentDetailsResponses;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentDetailsResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class AuthorEntityEnrollmentDetailsResponsesTest extends ScmIntegrationTest {

  @Test
  @DisplayName("입점 심사 상세 다건 조회 결과 객체를 응답 객체로 변환한다.")
  void fromTest() {
    // Given
    List<AuthorEnrollmentDetailsResult> list =
        Stream.of(
                createDetailsResult(),
                createDetailsResult(),
                createDetailsResult(),
                createDetailsResult(),
                createDetailsResult(),
                createDetailsResult(),
                createDetailsResult())
            .filter(result -> result.roleTypes().contains(RoleType.USER))
            .toList();

    Page<AuthorEnrollmentDetailsResult> results =
        new PageImpl<>(list, PageRequest.of(0, 10), list.size());

    // When
    AuthorEnrollmentDetailsResponses responses = AuthorEnrollmentDetailsResponses.from(results);

    // Then
    assertThat(responses.responses())
        .hasSize(list.size())
        .allSatisfy(
            response -> {
              assertThat(response.roleTypes()).contains(RoleType.USER);
              assertThat(response.enrollmentPassed()).isFalse();
              assertThat(response.profileImgUrl()).contains(String.valueOf(response.authorId()));
            });
  }

  private static AuthorEnrollmentDetailsResult createDetailsResult() {
    long authorId = System.currentTimeMillis();
    boolean isAuthorMember = System.currentTimeMillis() % 2 == 0;
    return new AuthorEnrollmentDetailsResult(
        authorId,
        System.currentTimeMillis(),
        List.of(isAuthorMember ? RoleType.AUTHOR : RoleType.USER),
        "profile_" + authorId + ".png",
        RandomValueProvider.createAuthorNickname() + authorId,
        "유튜브 대통령" + authorId,
        "https://www.instagram.com/pannibottle" + authorId,
        "escrow-service" + authorId + ".pdf",
        "mail-order-sales" + authorId + ".pdf",
        isAuthorMember,
        LocalDateTime.now());
  }
}
