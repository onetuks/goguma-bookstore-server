package com.onetuks.modulescm.author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulescm.ScmIntegrationTest;
import com.onetuks.modulescm.author.service.dto.result.AuthorEnrollmentDetailsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorEntityEnrollmentDetailsResultTest extends ScmIntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  private MemberEntity memberEntity;

  @BeforeEach
  void setUp() {
    memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("작가 엔티티에서 입점 심사 상세 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    AuthorEntity save = authorJpaRepository.save(AuthorFixture.create(memberEntity));

    // When
    AuthorEnrollmentDetailsResult result = AuthorEnrollmentDetailsResult.from(save);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(save.getAuthorId()),
        () -> assertThat(result.memberId()).isEqualTo(save.getMemberEntity().getMemberId()));
  }
}
