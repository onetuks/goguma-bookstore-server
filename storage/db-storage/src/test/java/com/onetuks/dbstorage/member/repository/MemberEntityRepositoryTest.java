package com.onetuks.dbstorage.member.repository;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.dbstorage.DbStorageIntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

class MemberEntityRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private MemberEntityRepository memberEntityRepository;

  @Test
  void create() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);

    // When
    final Member result = memberEntityRepository.create(member);

    // Then
    assertThat(result.authInfo().roles()).doesNotContain(RoleType.AUTHOR);
  }

  @Test
  void read() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(createId(), RoleType.USER));

    // When
    final Member result = memberEntityRepository.read(member.memberId());

    // Then
    assertThat(result.authInfo().roles()).doesNotContain(RoleType.AUTHOR);
  }

  @Test
  void read_WithAuthInfo() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(createId(), RoleType.USER));

    // When
    final Optional<Member> result =
        memberEntityRepository.read(
            member.authInfo().socialId(), member.authInfo().clientProvider());

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().authInfo().roles()).doesNotContain(RoleType.AUTHOR);
  }

  @Test
  void update() {
    // Given
    final Member member =
        memberEntityRepository.create(MemberFixture.create(createId(), RoleType.USER));
    final Member updatedMember =
        new Member(
            member.memberId(),
            member.authInfo(),
            member.nickname(),
            false,
            member.profileImgFilePath(),
            member.defaultAddressInfo());

    // When
    final Member result = memberEntityRepository.update(updatedMember);

    // Then
    assertThat(result.isAlarmPermitted()).isFalse();
  }

  @Test
  void delete() {
    // Given
    final Member member =
        memberEntityRepository.create(MemberFixture.create(createId(), RoleType.USER));

    // When
    memberEntityRepository.delete(member.memberId());

    // Then
    assertThatThrownBy(() -> memberEntityRepository.read(member.memberId()))
        .isInstanceOf(InvalidDataAccessApiUsageException.class);
  }
}
