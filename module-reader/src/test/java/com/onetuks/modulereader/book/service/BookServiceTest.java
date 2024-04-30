package com.onetuks.modulereader.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.book.vo.SortOrder;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.book.service.dto.result.BookGetResult;
import com.onetuks.modulereader.fixture.AuthorFixture;
import com.onetuks.modulereader.fixture.BookFixture;
import com.onetuks.modulereader.fixture.MemberFixture;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class BookServiceTest extends IntegrationTest {

  @Autowired private BookService bookService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private BookJpaRepository bookJpaRepository;

  private List<Book> books;

  @BeforeEach
  void setUp() {
    List<Author> authors =
        authorJpaRepository.saveAll(
            memberJpaRepository
                .saveAll(
                    IntStream.range(0, 3)
                        .mapToObj(i -> MemberFixture.create(RoleType.AUTHOR))
                        .toList())
                .stream()
                .map(AuthorFixture::create)
                .toList());

    books =
        bookJpaRepository.saveAll(
            IntStream.range(0, 20).mapToObj(i -> BookFixture.create(authors.get(i % 2))).toList());
  }

  @Test
  @DisplayName("도서 아이디로 도서를 조회하면 도서 조회 수 통계가 증가한다.")
  void readBookTest() {
    // Given
    int randomIndex = new Random().nextInt(books.size());
    long bookId = books.get(randomIndex).getBookId();
    Book expected = books.get(randomIndex);
    long expectedViewCount = expected.getBookStatics().getViewCount();

    // When
    BookGetResult result = bookService.readBook(bookId);

    // Then
    assertAll(
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
        () -> assertThat(result.coverImgUrl()).isEqualTo(expected.getCoverImgUrl()),
        () -> assertThat(result.detailImgUrls()).isEqualTo(expected.getDetailImgUrls()),
        () -> assertThat(result.previewUrls()).isEqualTo(expected.getPreviewUrls()),
        () ->
            assertThat(result.favoriteCount())
                .isEqualTo(expected.getBookStatics().getFavoriteCount()),
        () -> assertThat(result.viewCount()).isEqualTo(expectedViewCount + 1),
        () -> assertThat(result.salesCount()).isEqualTo(expected.getBookStatics().getSalesCount()),
        () ->
            assertThat(result.reviewCount()).isEqualTo(expected.getBookStatics().getReviewCount()),
        () ->
            assertThat(result.reviewScore()).isEqualTo(expected.getBookStatics().getReviewScore()));
  }

  @Test
  @DisplayName("검색조건을 모두 포함해서 품절제외와 프로모션 제품만 조회한다.")
  void readBooksTest() {
    // Given
    int index = new Random().nextInt(books.size());
    Book soldBook = books.get(index);

    String title = soldBook.getBookConceptualInfo().getTitle();
    String authorNickname = soldBook.getAuthorNickname();
    Category category =
        soldBook.getBookConceptualInfo().getCategories().stream().findAny().orElse(Category.NOVEL);
    boolean onlyPromotion = true;
    boolean exceptSoldOut = true;
    SortOrder sortOrder = SortOrder.PRICE_DESC;
    PageRequest pageable = PageRequest.of(0, 10);

    // When
    Page<BookGetResult> results =
        bookService.readBooks(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, sortOrder, pageable);

    // Then
    if (results.getTotalElements() >= 2) {
      BookGetResult expensiveBook = results.getContent().get(0);
      BookGetResult cheapBook = results.getContent().get((int) results.getTotalElements() - 1);
      assertThat(expensiveBook.purchasePrice()).isGreaterThan(cheapBook.purchasePrice());
    }

    assertThat(results)
        .allSatisfy(
            result -> {
              assertThat(result.title()).isEqualTo(title);
              assertThat(result.authorNickname()).isEqualTo(authorNickname);
              assertThat(result.categories()).contains(category);
              assertThat(result.promotion()).isTrue();
              assertThat(result.stockCount()).isPositive();
            });
  }

  @Test
  @DisplayName("검색 조건 없이 모든 도서 목록을 조회한다.")
  void readBooks_Test() {
    // Given & When
    Page<BookGetResult> results =
        bookService.readBooks(
            null, null, null, false, false, SortOrder.DATE, PageRequest.of(0, 10));

    // Then
    BookGetResult latestBook = results.getContent().get(0);
    BookGetResult initialBook = results.getContent().get((int) results.getTotalElements() - 1);

    assertThat(latestBook.bookId()).isGreaterThan(initialBook.bookId());
    assertThat(results).isNotEmpty().hasSize(10);
  }
}