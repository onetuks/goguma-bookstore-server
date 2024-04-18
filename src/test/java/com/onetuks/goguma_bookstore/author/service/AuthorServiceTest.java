package com.onetuks.goguma_bookstore.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorEditParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class AuthorServiceTest extends IntegrationTest {

  @Autowired private AuthorService authorService;

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private S3Service s3Service;

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
  void updateAuthorProfileTest() {
    // Given
    Author author = authors.get(0);
    AuthorEditParam param = new AuthorEditParam("빠니보틀", "유튜브 대통령");
    CustomFile customFile = CustomFileFixture.create(author.getAuthorId(), FileType.PROFILES);

    // When
    AuthorEditResult result =
        authorService.updateAuthorProfile(
            author.getAuthorId(), author.getAuthorId(), param, customFile);

    // Then
    File savedProfileImgFile = s3Service.getFile(customFile.getUri());

    assertAll(
        () -> assertThat(savedProfileImgFile).exists(),
        () -> assertThat(result.authorId()).isEqualTo(author.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).contains(String.valueOf(author.getAuthorId())),
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()));
  }

  @Test
  @DisplayName("로그인 작가 아이디와 요청 작가 아이디가 일치하지 않으면 예외를 던진다. 프로필 이미지는 저장되지 않는다.")
  void updateAuthorProfile_NotSameAuthorId_ExceptionTest() {
    // Given
    Author author0 = authors.get(0);
    Author author1 = authors.get(1);
    AuthorEditParam param = new AuthorEditParam("빠니보틀", "유튜브 대통령");
    CustomFile customFile = CustomFileFixture.create(author0.getAuthorId(), FileType.PROFILES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            authorService.updateAuthorProfile(
                author1.getAuthorId(), author0.getAuthorId(), param, customFile));
    assertThrows(NoSuchKeyException.class, () -> s3Service.getFile(customFile.getUri()));
  }

  @Test
  @DisplayName("작가 프로필 정보 조회한다.")
  void findAuthorDetailsTest() {
    // Given
    Author author = authors.get(0);

    // When
    AuthorDetailsResult result = authorService.findAuthorDetails(author.getAuthorId());

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(author.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).isEqualTo(author.getProfileImgUrl()),
        () -> assertThat(result.nickname()).isEqualTo(author.getNickname()),
        () -> assertThat(result.introduction()).isEqualTo(author.getIntroduction()));
  }

  @Test
  @DisplayName("작가 프로필 다건 조회한다.")
  void findAllAuthorDetailsTest() {
    // Given & When
    List<AuthorDetailsResult> results = authorService.findAllAuthorDetails();

    // Then
    List<Long> memberIds =
        authors.stream().map(author -> author.getMember().getMemberId()).toList();
    List<Long> authorIds = authors.stream().map(Author::getAuthorId).toList();

    assertThat(results)
        .hasSize(2)
        .allSatisfy(
            result -> {
              assertThat(result.authorId()).isIn(authorIds);
              assertThat(result.nickname()).contains("빠선생님");
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
        EntityNotFoundException.class, () -> authorService.findAuthorDetails(notExistAuthorId));
  }

  @Test
  @DisplayName("멤버 아이디로 작가 아이디를 조회한다.")
  void getAuthorIdByMemberIdTest() {
    // Given
    Author author = authors.get(0);

    // When
    Long authorId = authorService.getAuthorIdByMemberId(author.getMember().getMemberId());

    // Then
    assertThat(authorId).isEqualTo(author.getAuthorId());
  }

  @Test
  @DisplayName("작가 입점을 하지 않은 멤버 아이디로 작가 아이디 조회 시 예외가 발생한다.")
  void getAuthorIdByMemberId_NotAuthor_ExceptionTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.USER));

    // When & Then
    assertThrows(
        EntityNotFoundException.class,
        () -> authorService.getAuthorIdByMemberId(member.getMemberId()));
  }
}
