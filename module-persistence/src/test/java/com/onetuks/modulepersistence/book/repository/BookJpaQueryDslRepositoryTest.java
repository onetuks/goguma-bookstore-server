package com.onetuks.modulepersistence.book.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.modulepersistence.PersistenceIntegrationTest;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.model.embedded.EnrollmentInfo;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.model.embedded.BookConceptualInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPhysicalInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPriceInfo;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.book.vo.SortOrder;
import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.member.vo.AuthInfo;
import com.onetuks.modulepersistence.member.vo.UserData;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
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

    Random random = new Random();

    List<Author> authors =
        authorJpaRepository.saveAll(
            memberJpaRepository
                .saveAll(
                    IntStream.range(0, 3)
                        .mapToObj(
                            i -> {
                              String randomValue =
                                  String.valueOf(random.nextInt(1000000, 999999999));

                              return Member.builder()
                                  .authInfo(
                                      AuthInfo.from(
                                          UserData.builder()
                                              .name("빠니보틀")
                                              .clientProvider(
                                                  ClientProvider.values()[
                                                      random.nextInt(
                                                          ClientProvider.values().length)])
                                              .roleType(RoleType.AUTHOR)
                                              .socialId(randomValue)
                                              .build()))
                                  .alarmPermission(true)
                                  .nickname("빠니보틀" + i)
                                  .profileImgFilePath("profileImgFilePath" + i)
                                  .build();
                            })
                        .toList())
                .stream()
                .map(
                    member -> {
                      String randomValue = String.valueOf(random.nextInt(1000000, 999999999));

                      return Author.builder()
                          .member(member)
                          .profileImgFilePath("authorProfileImgFilePath" + member.getNickname())
                          .nickname(member.getNickname())
                          .introduction("유튜브 대통령")
                          .instagramUrl("https://www.instagram.com/" + member.getNickname())
                          .enrollmentInfo(
                              EnrollmentInfo.builder()
                                  .businessNumber(randomValue)
                                  .mailOrderSalesNumber(randomValue)
                                  .enrollmentAt(LocalDateTime.now())
                                  .enrollmentPassed(true)
                                  .build())
                          .build();
                    })
                .toList());

    bookJpaRepository.saveAll(
        IntStream.range(0, 20)
            .mapToObj(
                i -> {
                  String randomValue = String.valueOf(random.nextInt(1000000, 999999999));
                  Author author = authors.get(i % 2);
                  return Book.builder()
                      .author(author)
                      .authorNickname(author.getNickname())
                      .bookConceptualInfo(
                          BookConceptualInfo.builder()
                              .title("빠니보틀의 유라시아 여행기")
                              .publisher("샌드박스")
                              .categories(
                                  List.of(
                                      Category.values()[random.nextInt(Category.values().length)]))
                              .isbn(randomValue)
                              .oneLiner("빠니보틀의 유라시아 여행기")
                              .summary("빠니보틀의 유라시아 여행기")
                              .build())
                      .bookPhysicalInfo(
                          BookPhysicalInfo.builder()
                              .coverType("soft")
                              .height(10)
                              .width(10)
                              .pageCount(100L)
                              .build())
                      .bookPriceInfo(
                          BookPriceInfo.builder()
                              .stockCount(100L)
                              .regularPrice(10000L)
                              .purchasePrice(8000L)
                              .promotion(true)
                              .build())
                      .coverImgFilePath("coverImgFilePath" + i)
                      .detailImgFilePaths(List.of("detailImgFilePath" + i))
                      .previewFilePaths(List.of("previewFilePath" + i))
                      .build();
                })
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
    PageRequest pageable = PageRequest.of(0, 10);

    // When
    Page<Book> results =
        bookJpaQueryDslRepository.findByConditionsAndOrderByCriterias(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, sortOrder, pageable);

    // Then
    assertThat(results)
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
