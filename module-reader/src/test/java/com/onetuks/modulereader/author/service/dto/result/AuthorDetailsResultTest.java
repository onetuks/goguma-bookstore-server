package com.onetuks.modulereader.author.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.fixture.AuthorFixture;
import com.onetuks.modulereader.fixture.MemberFixture;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.author.service.dto.result.AuthorDetailsResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorDetailsResultTest extends IntegrationTest {

  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("작가 프로필 엔티티에서 결과 객체로 변환한다.")
  void fromTest() {
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
        () -> assertThat(result.introduction()).isEqualTo(savedAuthor.getIntroduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(savedAuthor.getInstagramUrl()),
        () -> assertThat(result.subscribeCount()).isZero(),
        () -> assertThat(result.bookCount()).isZero(),
        () -> assertThat(result.restockCount()).isZero());
  }
}
