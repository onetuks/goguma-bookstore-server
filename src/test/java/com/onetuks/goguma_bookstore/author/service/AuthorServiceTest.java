package com.onetuks.goguma_bookstore.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class AuthorServiceTest extends IntegrationTest {

  @Autowired private AuthorService authorService;

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
  @DisplayName("작가 프로필 정보 조회한다.")
  void readAuthorDetailsTest() {
    // Given
    Author author = authors.getFirst();

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
