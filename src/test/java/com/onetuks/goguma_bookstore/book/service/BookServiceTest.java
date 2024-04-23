package com.onetuks.goguma_bookstore.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.BookFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BookServiceTest extends IntegrationTest {

  @Autowired private BookService bookService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private BookJpaRepository bookJpaRepository;

  private List<Book> books;

  @BeforeEach
  void setUp() {
    List<Member> members =
        memberJpaRepository.saveAll(
            List.of(MemberFixture.create(RoleType.AUTHOR), MemberFixture.create(RoleType.AUTHOR)));
    List<Author> authors =
        authorJpaRepository.saveAll(members.stream().map(AuthorFixture::create).toList());

    books =
        bookJpaRepository.saveAll(
            IntStream.range(0, 10).mapToObj(i -> BookFixture.create(authors.get(i % 2))).toList());
  }

  @Test
  @DisplayName("도서 아이디로 도서를 조회한다.")
  void readBookTest() {
    // Given
    int randomIndex = new Random().nextInt(books.size());
    long bookId = books.get(randomIndex).getBookId();
    Book expected = books.get(randomIndex);

    // When
    BookResult result = bookService.readBook(bookId);

    // Then
    assertAll(
        () -> assertThat(result.bookId()).isEqualTo(expected.getBookId()),
        () -> assertThat(result.authorId()).isEqualTo(expected.getAuthor().getAuthorId()),
        () -> assertThat(result.authorNickname()).isEqualTo(expected.getAuthorNickname()),
        () -> assertThat(result.title()).isEqualTo(expected.getBookConceptualInfo().getTitle()),
        () ->
            assertThat(result.oneLiner()).isEqualTo(expected.getBookConceptualInfo().getOneLiner()),
        () -> assertThat(result.summary()).isEqualTo(expected.getBookConceptualInfo().getSummary()),
        () ->
            assertThat(result.categories())
                .isEqualTo(expected.getBookConceptualInfo().getCategories()),
        () -> assertThat(result.isbn()).isEqualTo(expected.getBookConceptualInfo().getIsbn()),
        () -> assertThat(result.height()).isEqualTo(expected.getBookPhysicalInfo().getHeight()),
        () -> assertThat(result.width()).isEqualTo(expected.getBookPhysicalInfo().getWidth()),
        () ->
            assertThat(result.pageCount()).isEqualTo(expected.getBookPhysicalInfo().getPageCount()),
        () ->
            assertThat(result.coverType()).isEqualTo(expected.getBookPhysicalInfo().getCoverType()),
        () ->
            assertThat(result.purchasePrice())
                .isEqualTo(expected.getBookPriceInfo().getPurchasePrice()),
        () ->
            assertThat(result.regularPrice())
                .isEqualTo(expected.getBookPriceInfo().getRegularPrice()),
        () -> assertThat(result.promotion()).isEqualTo(expected.getBookPriceInfo().getPromotion()),
        () -> assertThat(result.publisher()).isEqualTo(expected.getPublisher()),
        () -> assertThat(result.stockCount()).isEqualTo(expected.getStockCount()),
        () ->
            assertThat(result.coverImgUrl()).isEqualTo(expected.getCoverImgFile().getCoverImgUrl()),
        () -> assertThat(result.detailImgUrls()).isEqualTo(expected.getDetailImgUrls()),
        () -> assertThat(result.previewUrls()).isEqualTo(expected.getPreviewUrls()));
  }
}
