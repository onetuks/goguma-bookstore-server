package com.onetuks.readerdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class AuthorServiceTest extends ReaderDomainIntegrationTest {

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
    Page<Author> authors =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(
                    i ->
                        AuthorFixture.create(
                            createId(), MemberFixture.create(createId(), RoleType.USER)))
                .toList());

    Pageable pageable = PageRequest.of(0, 10);

    given(authorRepository.readAllEnrollmentPassed(pageable)).willReturn(authors);

    // When
    Page<Author> results = authorService.readAllAuthorDetails(pageable);

    // Then
    assertThat(results).hasSize(count);
  }
}
