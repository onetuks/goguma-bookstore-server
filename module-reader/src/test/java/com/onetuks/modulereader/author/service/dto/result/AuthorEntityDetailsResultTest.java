package com.onetuks.modulereader.author.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.ReaderIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthorEntityDetailsResultTest extends ReaderIntegrationTest {

  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("작가 프로필 엔티티에서 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberEntity authorMemberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    AuthorEntity savedAuthorEntity = authorJpaRepository.save(AuthorFixture.create(
        authorMemberEntity));

    // When
    AuthorDetailsResult result = AuthorDetailsResult.from(savedAuthorEntity);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(savedAuthorEntity.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(savedAuthorEntity.getProfileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(savedAuthorEntity.getNickname()),
        () -> assertThat(result.introduction()).isEqualTo(savedAuthorEntity.getIntroduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(savedAuthorEntity.getInstagramUrl()),
        () -> assertThat(result.subscribeCount()).isZero(),
        () -> assertThat(result.bookCount()).isZero(),
        () -> assertThat(result.restockCount()).isZero());
  }
}
