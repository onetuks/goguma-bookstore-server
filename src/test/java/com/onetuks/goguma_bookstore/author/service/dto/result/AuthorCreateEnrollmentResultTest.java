package com.onetuks.goguma_bookstore.author.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberRepository;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorCreateEnrollmentResultTest extends IntegrationTest {

  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private MemberRepository memberRepository;

  private Author author;

  @BeforeEach
  void setUp() throws IOException {
    Member member = memberRepository.save(MemberFixture.create(RoleType.USER));
    author = authorJpaRepository.save(AuthorFixture.create(member));
  }

  @Test
  @DisplayName("작가 엔티티에서 작가 생성 객체로 변환한다.")
  void from() {
    // When
    AuthorCreateEnrollmentResult result = AuthorCreateEnrollmentResult.from(author);

    // Then
    assertThat(result)
        .hasFieldOrPropertyWithValue("authorId", author.getAuthorId())
        .hasFieldOrPropertyWithValue("profileImgUrl", author.getProfileImgUrl());
  }
}
