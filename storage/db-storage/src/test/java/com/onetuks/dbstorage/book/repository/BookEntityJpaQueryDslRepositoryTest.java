package com.onetuks.dbstorage.book.repository;

import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.author.repository.AuthorJpaRepository;
import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.book.vo.SortOrder;
import com.onetuks.dbstorage.fixture.AuthorEntityFixture;
import com.onetuks.dbstorage.fixture.BookEntityFixture;
import com.onetuks.dbstorage.fixture.MemberEntityFixture;
import com.onetuks.dbstorage.member.repository.MemberJpaRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class BookEntityJpaQueryDslRepositoryTest extends DbStorageIntegrationTest {

  @Autowired private BookJpaQueryDslRepository bookJpaQueryDslRepository;

  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private BookJpaRepository bookJpaRepository;

  @BeforeEach
  void setUp() {
    List<AuthorEntity> authorEntities =
        authorJpaRepository.saveAll(
            memberJpaRepository
                .saveAll(
                    IntStream.range(0, 3)
                        .mapToObj(i -> MemberEntityFixture.create(createId(), RoleType.AUTHOR))
                        .toList())
                .stream()
                .map(memberEntity -> AuthorEntityFixture.create(createId(), memberEntity))
                .toList());

    bookJpaRepository.saveAll(
        IntStream.range(0, 20)
            .mapToObj(i -> BookEntityFixture.create(authorEntities.get(i % 2)))
            .toList());
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
    Pageable pageable = PageRequest.of(0, 10);

    // When
    Page<BookEntity> results =
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
              assertThat(result.getIsPromotion()).isTrue();
              assertThat(result.getStockCount()).isPositive();
            });
  }

  @Test
  @DisplayName("검색 조건 없이 모든 도서 목록을 조회한다.")
  void findByConditionsAndOrderByCriterias_Test() {
    // Given & When
    Page<BookEntity> results =
        bookJpaQueryDslRepository.findByConditionsAndOrderByCriterias(
            null, null, null, false, false, SortOrder.DATE, PageRequest.of(0, 10));

    // Then
    assertThat(results).isNotEmpty().hasSize(10);
  }
}
