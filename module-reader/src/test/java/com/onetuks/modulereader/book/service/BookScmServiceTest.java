package com.onetuks.modulereader.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.modulereader.IntegrationTest;
import com.onetuks.modulereader.book.service.BookScmService;
import com.onetuks.modulereader.book.service.dto.param.BookEditParam;
import com.onetuks.modulereader.book.service.dto.result.BookEditResult;
import com.onetuks.modulereader.book.service.dto.result.BookResult;
import com.onetuks.modulereader.fixture.AuthorFixture;
import com.onetuks.modulereader.fixture.FileWrapperFixture;
import com.onetuks.modulereader.fixture.MemberFixture;
import com.onetuks.modulereader.global.vo.file.FileType;
import com.onetuks.modulereader.global.vo.file.FileWrapper;
import com.onetuks.modulereader.global.vo.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulereader.registration.service.RegistrationScmService;
import com.onetuks.modulereader.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.modulereader.registration.service.dto.result.RegistrationResult;
import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;

class BookScmServiceTest extends IntegrationTest {

  private static final String BASE_URL = "https://test-bucket-url.com";

  @Autowired private BookScmService bookScmService;
  @Autowired private RegistrationScmService registrationScmService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private BookJpaRepository bookJpaRepository;

  private Author author;
  private RegistrationResult registrationResult;
  private Book book;
  private BookEditParam editParam;

  @BeforeEach
  void setUp() {
    author =
        authorJpaRepository.save(
            AuthorFixture.create(memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))));

    long authorId = author.getAuthorId();

    editParam =
        new BookEditParam(
            "대충 베트남에서 시작해서 영국까지",
            "이 편지는 영국에서 시작하여 대충 누구 손을 거쳐서 어쩌구 저쩌구 거시기 뭐시기",
            List.of(Category.NOVEL, Category.CARTOON),
            20_000L,
            10_000L,
            true,
            10L);

    registrationResult =
        registrationScmService.createRegistration(
            authorId,
            new RegistrationCreateParam(
                "유라시아 여행기",
                "대충 베트남에서 시작해서 영국까지",
                "이 편지는 영국에서 시작하여 대충 누구 손을 거쳐서 어쩌구 저쩌구 거시기 뭐시기",
                List.of(Category.NOVEL, Category.CARTOON),
                "978-89-12345-67-8",
                200,
                100,
                "양장본",
                500L,
                20_000L,
                10_000L,
                true,
                "출판사J",
                10L),
            FileWrapperFixture.createFile(authorId, FileType.COVERS),
            FileWrapperFixture.createFiles(authorId, FileType.DETAILS),
            FileWrapperFixture.createFiles(authorId, FileType.PREVIEWS),
            FileWrapperFixture.createFile(authorId, FileType.SAMPLES));

    registrationScmService.updateRegistrationApprovalInfo(
        registrationResult.registrationId(), true, "통과");

    book =
        bookJpaRepository.findAll().stream()
            .filter(
                bookEntity ->
                    Objects.equals(
                        bookEntity.getBookConceptualInfo().getIsbn(), registrationResult.isbn()))
            .findAny()
            .orElseThrow(() -> new EntityNotFoundException("테스트에서 도서 등록이 실패했습니다."));
  }

  @Test
  @DisplayName("도서 정보 수정 시 책 재고량이 0이 되고, 해당 신간등록이 검수 대기인 상태로 변경되며 신간등록 정보도 수정된다.")
  void updateBookTest() {
    // Given
    FileWrapper coverImgFile = FileWrapperFixture.createFile(author.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(author.getAuthorId(), FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(author.getAuthorId(), FileType.PREVIEWS);

    // When
    BookEditResult result =
        bookScmService.updateBook(
            author.getAuthorId(),
            book.getBookId(),
            editParam,
            coverImgFile,
            detailImgFiles,
            previewFiles);

    // Then
    assertAll(
        () -> assertThat(result.bookId()).isEqualTo(book.getBookId()),
        () -> assertThat(result.registrationId()).isEqualTo(registrationResult.registrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(registrationResult.approvalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo("도서 정보 수정으로 인한 재승인 필요"),
        () -> assertThat(result.title()).isEqualTo(book.getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(editParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editParam.summary()),
        () ->
            assertThat(result.categories())
                .containsExactlyInAnyOrderElementsOf(editParam.categories()),
        () -> assertThat(result.isbn()).isEqualTo(book.getIsbn()),
        () -> assertThat(result.height()).isEqualTo(book.getHeight()),
        () -> assertThat(result.width()).isEqualTo(book.getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(book.getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(book.getPageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(editParam.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(editParam.purchasePrice()),
        () -> assertThat(result.promotion()).isEqualTo(editParam.promotion()),
        () -> assertThat(result.publisher()).isEqualTo(book.getPublisher()),
        () -> assertThat(result.stockCount()).isEqualTo(editParam.stockCount()),
        () -> assertThat(result.coverImgUrl()).contains(coverImgFile.getUri()),
        () ->
            assertThat(result.detailImgUrls())
                .containsExactlyInAnyOrderElementsOf(
                    detailImgFiles.getUris().stream().map(uri -> BASE_URL + uri).toList()),
        () ->
            assertThat(result.previewUrls())
                .containsExactlyInAnyOrderElementsOf(
                    previewFiles.getUris().stream().map(uri -> BASE_URL + uri).toList()));
  }

  @Test
  @DisplayName("도서 정보 수정 시 책 재고량이 0이 되고, 해당 신간등록이 검수 대기인 상태로 변경되며 신간등록 정보도 수정된다.")
  void updateBook_NoFiles_Test() {
    // Given
    FileWrapper coverImgFile = FileWrapperFixture.createFile(author.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapperCollection previewFiles = new FileWrapperCollection(Collections.emptyList());

    // When
    BookEditResult result =
        bookScmService.updateBook(
            author.getAuthorId(),
            book.getBookId(),
            editParam,
            coverImgFile,
            detailImgFiles,
            previewFiles);

    // Then
    assertAll(
        () -> assertThat(result.bookId()).isEqualTo(book.getBookId()),
        () -> assertThat(result.registrationId()).isEqualTo(registrationResult.registrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(registrationResult.approvalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo("도서 정보 수정으로 인한 재승인 필요"),
        () -> assertThat(result.title()).isEqualTo(book.getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(editParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editParam.summary()),
        () ->
            assertThat(result.categories())
                .containsExactlyInAnyOrderElementsOf(editParam.categories()),
        () -> assertThat(result.isbn()).isEqualTo(book.getIsbn()),
        () -> assertThat(result.height()).isEqualTo(book.getHeight()),
        () -> assertThat(result.width()).isEqualTo(book.getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(book.getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(book.getPageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(editParam.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(editParam.purchasePrice()),
        () -> assertThat(result.promotion()).isEqualTo(editParam.promotion()),
        () -> assertThat(result.publisher()).isEqualTo(book.getPublisher()),
        () -> assertThat(result.stockCount()).isEqualTo(editParam.stockCount()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(book.getCoverImgUrl()),
        () ->
            assertThat(result.detailImgUrls())
                .containsExactlyInAnyOrderElementsOf(
                    detailImgFiles.getUris().stream().map(uri -> BASE_URL + uri).toList()),
        () ->
            assertThat(result.previewUrls())
                .containsExactlyInAnyOrderElementsOf(
                    previewFiles.getUris().stream().map(uri -> BASE_URL + uri).toList()));
  }

  @Test
  @DisplayName("도서에 대한 권한이 없는 작가가 도서 수정을 시도하면 예외를 던진다.")
  void updateBook_NotAuthorityAuthor_ExceptionTest() {
    // Given
    long notAuthorityAuthorId = 123_412_415L;

    // When & Then
    assertThrows(
        AccessDeniedException.class,
        () ->
            bookScmService.updateBook(
                notAuthorityAuthorId,
                book.getBookId(),
                editParam,
                FileWrapperFixture.createFile(notAuthorityAuthorId, FileType.COVERS),
                FileWrapperFixture.createFiles(notAuthorityAuthorId, FileType.DETAILS),
                FileWrapperFixture.createFiles(notAuthorityAuthorId, FileType.PREVIEWS)));
  }

  @Test
  @DisplayName("작가의 모든 도서를 조회한다.")
  void getAllBooksByAuthorTest() {
    // Given
    long loginAuthorId = author.getAuthorId();
    long authorId = author.getAuthorId();

    // When
    Page<BookResult> results =
        bookScmService.getAllBooksByAuthor(loginAuthorId, authorId, PageRequest.of(0, 10));

    // Then
    assertThat(results)
        .isNotNull()
        .isNotEmpty()
        .allSatisfy(
            result -> {
              assertThat(result.bookId()).isEqualTo(book.getBookId());
              assertThat(result.authorId()).isEqualTo(author.getAuthorId());
              assertThat(result.authorNickname()).isEqualTo(author.getNickname());
              assertThat(result.title()).isEqualTo(book.getTitle());
              assertThat(result.oneLiner()).isEqualTo(book.getOneLiner());
              assertThat(result.summary()).isEqualTo(book.getSummary());
              assertThat(result.categories())
                  .containsExactlyInAnyOrderElementsOf(book.getCategories());
              assertThat(result.isbn()).isEqualTo(book.getIsbn());
              assertThat(result.height()).isEqualTo(book.getHeight());
              assertThat(result.width()).isEqualTo(book.getWidth());
              assertThat(result.coverType()).isEqualTo(book.getCoverType());
              assertThat(result.pageCount()).isEqualTo(book.getPageCount());
              assertThat(result.regularPrice()).isEqualTo(book.getRegularPrice());
              assertThat(result.purchasePrice()).isEqualTo(book.getPurchasePrice());
              assertThat(result.promotion()).isEqualTo(book.isPromotion());
              assertThat(result.publisher()).isEqualTo(book.getPublisher());
              assertThat(result.stockCount()).isEqualTo(book.getStockCount());
              assertThat(result.coverImgUrl()).isEqualTo(book.getCoverImgUrl());
              assertThat(result.detailImgUrls())
                  .containsExactlyInAnyOrderElementsOf(book.getDetailImgUrls());
              assertThat(result.previewUrls())
                  .containsExactlyInAnyOrderElementsOf(book.getPreviewUrls());
            });
  }

  @Test
  @DisplayName("조회하려는 작가와 로그인한 작가가 다르면 시스템 도서 조회 시 예외를 던진다.")
  void getAllBooksByAuthor_NotAuthorityAuthor_ExceptionTest() {
    // Given
    long loginAuthorId = 123_421_415L;
    long authorId = author.getAuthorId();

    // When
    assertThrows(
        AccessDeniedException.class,
        () -> bookScmService.getAllBooksByAuthor(loginAuthorId, authorId, PageRequest.of(0, 10)));
  }
}
