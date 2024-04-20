package com.onetuks.goguma_bookstore.registration.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.RegistrationFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegistrationInspectionResultTest extends IntegrationTest {

  @Autowired private RegistrationJpaRepository registrationJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  @Test
  @DisplayName("신간등록 엔티티에서 신간등록 검수 결과 객체로 변환한다.")
  void from() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author author = authorJpaRepository.save(AuthorFixture.create(member));
    Registration registration = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    RegistrationInspectionResult result = RegistrationInspectionResult.from(registration);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(registration.getRegistrationId()),
        () ->
            assertThat(result.approvalResult())
                .isEqualTo(registration.getApprovalInfo().getApprovalResult()),
        () ->
            assertThat(result.approvalMemo())
                .isEqualTo(registration.getApprovalInfo().getApprovalMemo()));
  }
}
