package com.onetuks.modulescm.registration.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.fixture.RegistrationFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.registration.entity.RegistrationEntity;
import com.onetuks.modulepersistence.registration.repository.RegistrationJpaRepository;
import com.onetuks.modulescm.ScmIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegistrationEntityInspectionResultTest extends ScmIntegrationTest {

  @Autowired private RegistrationJpaRepository registrationJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  @Test
  @DisplayName("신간등록 엔티티에서 신간등록 검수 결과 객체로 변환한다.")
  void from() {
    // Given
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    AuthorEntity authorEntity = authorJpaRepository.save(AuthorFixture.create(memberEntity));
    RegistrationEntity registrationEntity = registrationJpaRepository.save(RegistrationFixture.create(
        authorEntity));

    // When
    RegistrationInspectionResult result = RegistrationInspectionResult.from(registrationEntity);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(registrationEntity.getRegistrationId()),
        () ->
            assertThat(result.approvalResult())
                .isEqualTo(registrationEntity.getApprovalInfo().getApprovalResult()),
        () ->
            assertThat(result.approvalMemo())
                .isEqualTo(registrationEntity.getApprovalInfo().getApprovalMemo()));
  }
}
