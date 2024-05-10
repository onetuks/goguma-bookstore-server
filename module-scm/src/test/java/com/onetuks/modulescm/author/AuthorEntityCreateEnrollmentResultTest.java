package com.onetuks.modulescm.author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulescm.ScmIntegrationTest;
import com.onetuks.modulescm.author.service.dto.result.AuthorCreateEnrollmentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorEntityCreateEnrollmentResultTest extends ScmIntegrationTest {

  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;

  private AuthorEntity authorEntity;

  @BeforeEach
  void setUp() {
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    authorEntity = authorJpaRepository.save(AuthorFixture.create(memberEntity));
  }

  @Test
  @DisplayName("작가 엔티티에서 작가 생성 객체로 변환한다.")
  void from() {
    // When
    AuthorCreateEnrollmentResult result = AuthorCreateEnrollmentResult.from(authorEntity);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(authorEntity.getAuthorId()),
        () -> assertThat(result.nickname()).isEqualTo(authorEntity.getNickname()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(authorEntity.getProfileImgUrl()),
        () -> assertThat(result.introduction()).isEqualTo(authorEntity.getIntroduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(authorEntity.getInstagramUrl()),
        () ->
            assertThat(result.businessNumber())
                .isEqualTo(authorEntity.getEnrollmentInfo().getBusinessNumber()),
        () ->
            assertThat(result.mailOrderSalesNumber())
                .isEqualTo(authorEntity.getEnrollmentInfo().getMailOrderSalesNumber()));
  }
}
