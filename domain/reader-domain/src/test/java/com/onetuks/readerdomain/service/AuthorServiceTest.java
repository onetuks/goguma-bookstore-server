package com.onetuks.readerdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorRepository;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import com.onetuks.readerdomain.author.service.AuthorService;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class AuthorServiceTest extends ReaderDomainIntegrationTest {

  @Autowired private AuthorService authorService;

  @MockBean private AuthorRepository authorRepository;

  @Test
  @DisplayName("작가 프로필 정보 조회한다.")
  void readAuthorDetailsTest() {
    // Given
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.USER));

    given(authorRepository.read(author.authorId())).willReturn(author);

    // When
    Author result = authorService.readAuthorDetails(author.authorId());

    // Then
    assertAll(
        () -> assertThat(result.authorId()).isEqualTo(author.authorId()),
        () -> assertThat(result.profileImgFilePath()).isEqualTo(author.profileImgFilePath()),
        () -> assertThat(result.nickname()).isEqualTo(author.nickname()),
        () -> assertThat(result.introduction()).isEqualTo(author.introduction()));
  }

  @Test
  @DisplayName("작가 프로필 다건 조회한다.")
  void readAllAuthorDetailsTest() {
    // Given
    int count = 5;
    List<Author> authors =
        IntStream.range(0, count)
            .mapToObj(
                i ->
                    AuthorFixture.create(
                        createId(), MemberFixture.create(createId(), RoleType.USER)))
            .toList();

    given(authorRepository.readAllEnrollmentPassed()).willReturn(authors);

    // When
    List<Author> results = authorService.readAllAuthorDetails();

    // Then
    assertThat(results).hasSize(count);
  }
}
