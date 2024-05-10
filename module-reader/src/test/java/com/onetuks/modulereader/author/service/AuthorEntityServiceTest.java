package com.onetuks.modulereader.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.service.S3Repository;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.ReaderIntegrationTest;
import com.onetuks.modulereader.author.service.dto.param.AuthorEditParam;
import com.onetuks.modulereader.author.service.dto.result.AuthorDetailsResult;
import com.onetuks.modulereader.author.service.dto.result.AuthorEditResult;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class AuthorEntityServiceTest extends ReaderIntegrationTest {

  @Autowired private AuthorService authorService;
  @Autowired private S3Repository s3Repository;

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  private List<AuthorEntity> authorEntities;

  @BeforeEach
  void setUp() {
    List<MemberEntity> memberEntities =
        List.of(
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.AUTHOR),
            MemberFixture.create(RoleType.AUTHOR));

    authorEntities =
        authorJpaRepository.saveAll(
            memberJpaRepository.saveAll(memberEntities).stream()
                .map(
                    member ->
                        AuthorFixture.createWithEnrollmentAt(
                            member, LocalDateTime.now().minusWeeks(2).minusHours(1)))
                .toList());
  }

  @Test
  @DisplayName("작가 정보를 수정한다. 작가 프로필은 S3에 저장된다.")
  void changeAuthorProfileTest() {
    // Given
    MemberEntity authorMemberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    AuthorEntity authorEntity = authorJpaRepository.save(AuthorFixture.create(authorMemberEntity));
    AuthorEditParam param =
        new AuthorEditParam("빠니보틀", "유튜브 대통령", "https://www.instagram.com/pannibottle");
    FileWrapper fileWrapper =
        FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.PROFILES);

    // When
    AuthorEditResult result =
        authorService.updateAuthorProfile(
            authorEntity.getAuthorId(), authorEntity.getAuthorId(), param, fileWrapper);

    // Then
    File savedProfileImgFile = s3Repository.getFile(fileWrapper.getUri());

    assertAll(
        () -> assertThat(savedProfileImgFile).exists(),
        () -> assertThat(result.authorId()).isEqualTo(authorEntity.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).contains(String.valueOf(authorEntity.getAuthorId())),
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()));
  }

  @Test
  @DisplayName("로그인 작가 아이디와 요청 작가 아이디가 일치하지 않으면 예외를 던진다. 프로필 이미지는 저장되지 않는다.")
  void changeAuthorProfile_NotSameAuthorId_ExceptionTest() {
    // Given
    MemberEntity authorMemberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    AuthorEntity authorEntity0 = authorJpaRepository.save(AuthorFixture.create(authorMemberEntity));
    AuthorEntity authorEntity1 =
        authorJpaRepository.save(
            AuthorFixture.create(memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))));
    AuthorEditParam param =
        new AuthorEditParam("빠니보틀", "유튜브 대통령", "https://www.instagram.com/pannibottle");
    FileWrapper fileWrapper =
        FileWrapperFixture.createFile(authorEntity0.getAuthorId(), FileType.PROFILES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            authorService.updateAuthorProfile(
                authorEntity1.getAuthorId(), authorEntity0.getAuthorId(), param, fileWrapper));
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(fileWrapper.getUri()));
  }

  @Test
  @DisplayName("작가 프로필 정보 조회한다.")
  void readAuthorDetailsTest() {
    // Given
    AuthorEntity authorEntity = authorEntities.get(0);

    // When
    AuthorDetailsResult result = authorService.readAuthorDetails(authorEntity.getAuthorId());

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(authorEntity.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(authorEntity.getProfileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(authorEntity.getNickname()),
        () -> assertThat(result.introduction()).isEqualTo(authorEntity.getIntroduction()));
  }

  @Test
  @DisplayName("작가 프로필 다건 조회한다.")
  void readAllAuthorDetailsTest() {
    // Given & When
    Page<AuthorDetailsResult> results = authorService.readAllAuthorDetails(PageRequest.of(0, 10));

    // Then
    List<Long> memberIds =
        authorEntities.stream().map(author -> author.getMemberEntity().getMemberId()).toList();
    List<Long> authorIds = authorEntities.stream().map(AuthorEntity::getAuthorId).toList();

    assertThat(results)
        .hasSize(2)
        .allSatisfy(
            result -> {
              assertThat(result.authorId()).isIn(authorIds);
              assertThat(result.introduction()).contains("대통령");
              assertThat(
                      memberIds.stream()
                          .anyMatch(
                              memberId ->
                                  result.profileImgUrl().contains(String.valueOf(memberId))))
                  .isTrue();
            });
  }

  @Test
  @DisplayName("존재하지 않는 작가의 아이디로 조회할 때 예외가 발생한다.")
  void getAuthorById_NotExistAuthorId_ExceptionTest() {
    // Given
    long notExistAuthorId = 1_213_300L;

    // When & Then
    assertThrows(
        EntityNotFoundException.class, () -> authorService.readAuthorDetails(notExistAuthorId));
  }
}
