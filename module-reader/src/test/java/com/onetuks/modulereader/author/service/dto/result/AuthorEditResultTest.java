package com.onetuks.modulereader.author.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorEditResultTest extends IntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  @Test
  @DisplayName("작가 프로필 수정 엔티티에서 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author author = authorJpaRepository.save(AuthorFixture.create(member));

    // When
    AuthorEditResult result = AuthorEditResult.from(author);

    // Then
    assertAll(
        () -> assertThat(result.profileImgUrl()).isEqualTo(author.getProfileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(author.getNickname()),
        () -> assertThat(result.introduction()).isEqualTo(author.getIntroduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(author.getInstagramUrl()));
  }
}
