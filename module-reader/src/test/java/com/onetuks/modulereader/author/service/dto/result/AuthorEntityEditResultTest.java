package com.onetuks.modulereader.author.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

class AuthorEntityEditResultTest extends ReaderIntegrationTest {

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  @Test
  @DisplayName("작가 프로필 수정 엔티티에서 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    AuthorEntity authorEntity = authorJpaRepository.save(AuthorFixture.create(memberEntity));

    // When
    AuthorEditResult result = AuthorEditResult.from(authorEntity);

    // Then
    assertAll(
        () -> assertThat(result.profileImgUrl()).isEqualTo(authorEntity.getProfileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(authorEntity.getNickname()),
        () -> assertThat(result.introduction()).isEqualTo(authorEntity.getIntroduction()),
        () -> assertThat(result.instagramUrl()).isEqualTo(authorEntity.getInstagramUrl()));
  }
}
