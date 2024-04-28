package com.onetuks.goguma_bookstore.author.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorCreateEnrollmentResultTest extends IntegrationTest {

  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;

  private Author author;

  @BeforeEach
  void setUp() {
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));
    author = authorJpaRepository.save(AuthorFixture.create(member));
  }

  @Test
  @DisplayName("작가 엔티티에서 작가 생성 객체로 변환한다.")
  void from() {
    // When
    AuthorCreateEnrollmentResult result = AuthorCreateEnrollmentResult.from(author);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(author.getAuthorId()),
        () -> assertThat(result.nickname()).isEqualTo(author.getNickname()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(author.getProfileImgUrl()),
        () -> assertThat(result.introduction()).isEqualTo(author.getIntroduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(author.getInstagramUrl()),
        () ->
            assertThat(result.businessNumber())
                .isEqualTo(author.getEnrollmentInfo().getBusinessNumber()),
        () ->
            assertThat(result.mailOrderSalesNumber())
                .isEqualTo(author.getEnrollmentInfo().getMailOrderSalesNumber()));
  }
}
