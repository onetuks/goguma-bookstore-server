package com.onetuks.goguma_bookstore.author.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorDetailsResultTest extends IntegrationTest {

  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("작가 프로필 엔티티에서 결과 객체로 변환한다.")
  void fromTest() throws IOException {
    // Given
    Member authorMember = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author savedAuthor = authorJpaRepository.save(AuthorFixture.create(authorMember));

    // When
    AuthorDetailsResult result = AuthorDetailsResult.from(savedAuthor);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(savedAuthor.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(savedAuthor.getProfileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(savedAuthor.getNickname()),
        () -> assertThat(result.introduction()).isEqualTo(savedAuthor.getIntroduction()));
  }
}
