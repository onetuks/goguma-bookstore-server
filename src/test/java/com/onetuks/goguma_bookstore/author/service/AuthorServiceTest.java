package com.onetuks.goguma_bookstore.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.auth.repository.MemberRepository;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorEditParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.MultipartFileFixture;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

class AuthorServiceTest extends IntegrationTest {

  @Autowired private AuthorService authorService;

  @Autowired private MemberRepository memberRepository;
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
            memberRepository.saveAll(members).stream()
                .map(
                    member -> {
                      try {
                        return AuthorFixture.createWithEnrollmentAt(
                            member, LocalDateTime.now().minusWeeks(2).minusHours(1));
                      } catch (IOException e) {
                        // ignore
                      }
                      return null;
                    })
                .filter(Objects::nonNull)
                .toList());
  }

  @Test
  @DisplayName("작가 정보를 수정한다.")
  void updateAuthorProfileTest() throws IOException {
    // Given
    Author author = authors.get(0);
    AuthorEditParam param = new AuthorEditParam("빠니보틀", "유튜브 대통령");
    MultipartFile profileImg =
        MultipartFileFixture.createFile(FileType.PROFILES, author.getAuthorId());

    // When
    AuthorEditResult result =
        authorService.updateAuthorProfile(
            author.getAuthorId(), author.getAuthorId(), param, profileImg);

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(author.getAuthorId()),
        () -> assertThat(result.profileImgUrl()).contains(String.valueOf(author.getAuthorId())),
        () -> assertThat(result.nickname()).isEqualTo(param.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()));
  }

  @Test
  @DisplayName("로그인 작가 아이디와 요청 작가 아이디가 일치하지 않으면 예외를 던진다.")
  void updateAuthorProfile_NotSameAuthorId_ExceptionTest() throws IOException {
    // Given
    Author author0 = authors.get(0);
    Author author1 = authors.get(1);
    AuthorEditParam param = new AuthorEditParam("빠니보틀", "유튜브 대통령");
    MultipartFile profileImg =
        MultipartFileFixture.createFile(FileType.PROFILES, author0.getAuthorId());

    // When & Then
    assertThatThrownBy(
            () ->
                authorService.updateAuthorProfile(
                    author1.getAuthorId(), author0.getAuthorId(), param, profileImg))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
