package com.onetuks.scmdomain.restock.service;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.BookFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RestockFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.model.BookStatics;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.scmdomain.ScmDomainIntegrationTest;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class RestockScmServiceTest extends ScmDomainIntegrationTest {

  @Test
  void readRestockBookCount() {
    // Given
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));
    List<Restock> restocks =
        IntStream.range(0, 5)
            .mapToObj(
                i ->
                    RestockFixture.create(
                        createId(), member, BookFixture.create(createId(), author)))
            .toList();
    Page<Book> books =
        new PageImpl<>(
            restocks.stream()
                .map(Restock::book)
                .map(
                    book ->
                        new Book(
                            book.bookId(),
                            book.author(),
                            book.bookConceptualInfo(),
                            book.bookPhysicalInfo(),
                            book.bookPriceInfo(),
                            book.coverImgFilePath(),
                            book.detailImgFilePaths(),
                            book.previewFilePaths(),
                            new BookStatics(
                                book.bookStatics().bookStaticsId(),
                                book.bookStatics().favoriteCount(),
                                book.bookStatics().viewCount(),
                                book.bookStatics().salesCount(),
                                book.bookStatics().restockCount() + 1)))
                .toList());

    given(authorScmRepository.readByMember(author.member().memberId())).willReturn(author);
    given(bookScmRepository.readAll(author.authorId(), Pageable.unpaged())).willReturn(books);

    // When
    long result = restockScmService.readRestockBookCount(author.member().memberId());

    // Then
    assertThat(result).isEqualTo(restocks.size());
  }

  @Test
  void readAllRestockBooks() {
    // Given
    int count = 5;
    Member member = MemberFixture.create(createId(), RoleType.USER);
    Author author =
        AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR));
    Page<Book> books =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(
                    i ->
                        RestockFixture.create(
                            createId(), member, BookFixture.create(createId(), author)))
                .map(Restock::book)
                .toList());

    given(authorScmRepository.readByMember(member.memberId())).willReturn(author);
    given(bookScmRepository.readAll(author.authorId(), PageRequest.of(0, 10))).willReturn(books);

    // When
    Page<Book> results =
        restockScmService.readAllRestockBooks(member.memberId(), PageRequest.of(0, 10));

    // Then
    assertThat(results.getTotalElements()).isEqualTo(count);
  }
}
