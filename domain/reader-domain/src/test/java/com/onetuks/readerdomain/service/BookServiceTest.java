package com.onetuks.readerdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createCategories;
import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static com.onetuks.coredomain.util.TestValueProvider.createNickname;
import static com.onetuks.coredomain.util.TestValueProvider.createTitle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.BookFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.model.BookStatics;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.book.PageOrder;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class BookServiceTest extends ReaderDomainIntegrationTest {

  @Test
  @DisplayName("도서를 조회하고 조회수는 증가한다.")
  void readBookTest() {
    // Given
    Book book =
        BookFixture.create(
            createId(),
            AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR)));
    Book expected =
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
                book.bookStatics().viewCount() + 1,
                book.bookStatics().salesCount(),
                book.bookStatics().commentCount()));

    given(bookRepository.read(book.bookId())).willReturn(expected);

    // When
    Book result = bookService.readBook(book.bookId());

    // Then
    assertThat(result.bookStatics().viewCount()).isOne();
  }

  @Test
  @DisplayName("검색조건에 부합하는 도서를 조회한다.")
  void readBooksTest() {
    // Given
    String title = createTitle();
    String authorNickname = createNickname().nicknameValue();
    Category category = createCategories().get(0);
    boolean onlyPromotion = new Random().nextBoolean();
    boolean exceptSoldOut = new Random().nextBoolean();
    PageOrder pageOrder = PageOrder.COMMENT;

    int count = 5;
    Page<Book> books =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(
                    i ->
                        BookFixture.create(
                            createId(),
                            AuthorFixture.create(
                                createId(), MemberFixture.create(createId(), RoleType.AUTHOR))))
                .toList());

    Pageable pageable = PageRequest.of(0, 10);

    given(
            bookRepository.readAll(
                title, authorNickname, category, onlyPromotion, exceptSoldOut, pageOrder, pageable))
        .willReturn(books);

    // When
    Page<Book> results =
        bookService.readBooks(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, pageOrder, pageable);

    // Then
    assertThat(results).hasSize(count);
  }
}
