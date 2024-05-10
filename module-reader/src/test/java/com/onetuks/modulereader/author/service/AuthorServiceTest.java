package com.onetuks.modulereader.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.service.S3Repository;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
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

class AuthorServiceTest extends ReaderIntegrationTest {

  @Autowired private AuthorService authorService;
  @Autowired private S3Repository s3Repository;

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  private List<Author> authors;

  @BeforeEach
  void setUp() {
    List<Member> members =
        List.of(
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.USER),
            MemberFixture.create(RoleType.AUTHOR),
            MemberFixture.create(RoleType.AUTHOR));

    authors =
        authorJpaRepository.saveAll(
            memberJpaRepository.saveAll(members).stream()
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
    Member authorMember = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author author = authorJpaRepository.save(AuthorFixture.create(authorMember));
    AuthorEditParam param =
        new AuthorEditParam("빠니보틀", "유튜브 대통령", "https://www.instagram.com/pannibottle");
    FileWrapper fileWrapper =
        FileWrapperFixture.createFile(author.getAuthorId(), FileType.PROFILES);

    // When
    AuthorEditResult result =
        authorService.updateAuthorProfile(
            author.getAuthorId(), author.getAuthorId(), param, fileWrapper);

    // Then
    File savedProfileImgFile = s3Repository.getFile(fileWrapper.getUri());

    assertAll(
        () -> assertThat(savedProfileImgFile).exists(),
        () -> assertThat(result.authorId()).isEqualTo(author.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).contains(String.valueOf(author.getAuthorId())),
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()));
  }

  @Test
  @DisplayName("로그인 작가 아이디와 요청 작가 아이디가 일치하지 않으면 예외를 던진다. 프로필 이미지는 저장되지 않는다.")
  void changeAuthorProfile_NotSameAuthorId_ExceptionTest() {
    // Given
    Member authorMember = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author author0 = authorJpaRepository.save(AuthorFixture.create(authorMember));
    Author author1 =
        authorJpaRepository.save(
            AuthorFixture.create(memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))));
    AuthorEditParam param =
        new AuthorEditParam("빠니보틀", "유튜브 대통령", "https://www.instagram.com/pannibottle");
    FileWrapper fileWrapper =
        FileWrapperFixture.createFile(author0.getAuthorId(), FileType.PROFILES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            authorService.updateAuthorProfile(
                author1.getAuthorId(), author0.getAuthorId(), param, fileWrapper));
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(fileWrapper.getUri()));
  }

  @Test
  @DisplayName("작가 프로필 정보 조회한다.")
  void readAuthorDetailsTest() {
    // Given
    Author author = authors.get(0);

    // When
    AuthorDetailsResult result = authorService.readAuthorDetails(author.getAuthorId());

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(author.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(author.getProfileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(author.getNickname()),
        () -> assertThat(result.introduction()).isEqualTo(author.getIntroduction()));
  }

  @Test
  @DisplayName("작가 프로필 다건 조회한다.")
  void readAllAuthorDetailsTest() {
    // Given & When
    Page<AuthorDetailsResult> results = authorService.readAllAuthorDetails(PageRequest.of(0, 10));

    // Then
    List<Long> memberIds =
        authors.stream().map(author -> author.getMember().getMemberId()).toList();
    List<Long> authorIds = authors.stream().map(Author::getAuthorId).toList();

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
