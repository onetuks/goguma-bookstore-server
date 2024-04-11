package com.onetuks.goguma_bookstore.author.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

class AuthorEnrollmentDetailsResultTest extends IntegrationTest {

  @Autowired private MemberRepository memberRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  private Member member;

  @BeforeEach
  void setUp() {
    member = memberRepository.save(MemberFixture.create(RoleType.AUTHOR));
  }

  @Test
  @DisplayName("작가 엔티티에서 입점 심사 상세 결과 객체로 변환한다.")
  void from() throws IOException {
    // Given
    Author save = authorJpaRepository.save(AuthorFixture.create(member));

    // When
    AuthorEnrollmentDetailsResult result = AuthorEnrollmentDetailsResult.from(save);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(save.getAuthorId()),
        () -> assertThat(result.memberId()).isEqualTo(save.getMember().getMemberId()));
  }
}