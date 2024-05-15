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
import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.book.PageOrder;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.readerdomain.ReaderDomainIntegrationTest;
import com.onetuks.readerdomain.book.service.BookService;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class BookServiceTest extends ReaderDomainIntegrationTest {

  @Autowired private BookService bookService;

  @MockBean private BookRepository bookRepository;

  @Test
  @DisplayName("도서를 조회하고 조회수는 증가한다.")
  void readBookTest() {
    // Given
    Book book =
        BookFixture.create(
            createId(),
            AuthorFixture.create(createId(), MemberFixture.create(createId(), RoleType.AUTHOR)));

    given(bookRepository.read(book.bookId())).willReturn(book);

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
    List<Book> books =
        IntStream.range(0, count)
            .mapToObj(
                i ->
                    BookFixture.create(
                        createId(),
                        AuthorFixture.create(
                            createId(), MemberFixture.create(createId(), RoleType.AUTHOR))))
            .toList();

    given(
            bookRepository.read(
                title, authorNickname, category, onlyPromotion, exceptSoldOut, pageOrder))
        .willReturn(books);

    // When
    List<Book> results =
        bookService.readBooks(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, pageOrder);

    // Then
    assertThat(results).hasSize(count);
  }
}
