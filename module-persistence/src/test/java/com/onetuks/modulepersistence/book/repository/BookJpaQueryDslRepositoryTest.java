package com.onetuks.modulepersistence.book.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.modulepersistence.PersistenceIntegrationTest;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.book.vo.SortOrder;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.BookFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class BookJpaQueryDslRepositoryTest extends PersistenceIntegrationTest {

  @Autowired private BookJpaQueryDslRepository bookJpaQueryDslRepository;

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private BookJpaRepository bookJpaRepository;

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

    bookJpaRepository.saveAll(
        IntStream.range(0, 20).mapToObj(i -> BookFixture.create(authors.get(i % 2))).toList());
  }

  @Test
  @DisplayName("검색조건을 모두 포함해서 품절제외와 프로모션 제품만 조회한다.")
  void findByConditionsAndOrderByCriteriasTest() {
    // Given
    String title = "빠니보틀 유라시아 여행기";
    String authorNickname = "빠니보틀";
    Category category = Category.NOVEL;
    boolean onlyPromotion = true;
    boolean exceptSoldOut = true;
    SortOrder sortOrder = SortOrder.PRICE_DESC;
    PageRequest pageable = PageRequest.of(0, 10);

    // When
    Page<Book> results =
        bookJpaQueryDslRepository.findByConditionsAndOrderByCriterias(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, sortOrder, pageable);

    // Then
    assertThat(results)
        .hasSizeGreaterThanOrEqualTo(0)
        .allSatisfy(
            result -> {
              assertThat(result.getTitle()).isEqualTo(title);
              assertThat(result.getAuthorNickname()).isEqualTo(authorNickname);
              assertThat(result.getCategories()).contains(category);
              assertThat(result.isPromotion()).isTrue();
              assertThat(result.getStockCount()).isPositive();
            });
  }

  @Test
  @DisplayName("검색 조건 없이 모든 도서 목록을 조회한다.")
  void findByConditionsAndOrderByCriterias_Test() {
    // Given & When
    Page<Book> results =
        bookJpaQueryDslRepository.findByConditionsAndOrderByCriterias(
            null, null, null, false, false, SortOrder.DATE, PageRequest.of(0, 10));

    // Then
    assertThat(results).isNotEmpty().hasSize(10);
  }
}
