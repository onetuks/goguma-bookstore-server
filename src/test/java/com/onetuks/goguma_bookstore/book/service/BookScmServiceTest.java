package com.onetuks.goguma_bookstore.book.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.book.service.dto.param.BookEditParam;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookEditResult;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.registration.service.RegistrationScmService;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationResult;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

class BookScmServiceTest extends IntegrationTest {

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
            CustomFileFixture.createFile(authorId, FileType.COVERS),
            CustomFileFixture.createFiles(authorId, FileType.DETAILS),
            CustomFileFixture.createFiles(authorId, FileType.PREVIEWS),
            CustomFileFixture.createFile(authorId, FileType.SAMPLES));

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
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> detailImgFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), FileType.DETAILS);
    List<CustomFile> previewFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), FileType.PREVIEWS);

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
        () -> assertThat(result.title()).isEqualTo(book.getBookConceptualInfo().getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(editParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editParam.summary()),
        () ->
            assertThat(result.categories())
                .containsExactlyInAnyOrderElementsOf(editParam.categories()),
        () -> assertThat(result.isbn()).isEqualTo(book.getBookConceptualInfo().getIsbn()),
        () -> assertThat(result.height()).isEqualTo(book.getBookPhysicalInfo().getHeight()),
        () -> assertThat(result.width()).isEqualTo(book.getBookPhysicalInfo().getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(book.getBookPhysicalInfo().getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(book.getBookPhysicalInfo().getPageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(editParam.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(editParam.purchasePrice()),
        () -> assertThat(result.promotion()).isEqualTo(editParam.promotion()),
        () -> assertThat(result.publisher()).isEqualTo(book.getPublisher()),
        () -> assertThat(result.stockCount()).isEqualTo(editParam.stockCount()),
        () ->
            assertThat(result.coverImgUrl())
                .isEqualTo(coverImgFile.toCoverImgFile().getCoverImgUrl()),
        () ->
            assertThat(result.detailImgUrls())
                .containsExactlyInAnyOrderElementsOf(
                    detailImgFiles.stream()
                        .map(file -> file.toDetailImgFile().getDetailImgUrl())
                        .toList()),
        () ->
            assertThat(result.previewUrls())
                .containsExactlyInAnyOrderElementsOf(
                    previewFiles.stream()
                        .map(file -> file.toPreviewFile().getPreviewUrl())
                        .toList()));
  }

  @Test
  @DisplayName("도서 정보 수정 시 책 재고량이 0이 되고, 해당 신간등록이 검수 대기인 상태로 변경되며 신간등록 정보도 수정된다.")
  void updateBook_NoFiles_Test() {
    // Given
    CustomFile coverImgFile = CustomFileFixture.createNullFile();
    List<CustomFile> detailImgFiles = List.of();
    List<CustomFile> previewFiles = List.of();

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
        () -> assertThat(result.title()).isEqualTo(book.getBookConceptualInfo().getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(editParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editParam.summary()),
        () ->
            assertThat(result.categories())
                .containsExactlyInAnyOrderElementsOf(editParam.categories()),
        () -> assertThat(result.isbn()).isEqualTo(book.getBookConceptualInfo().getIsbn()),
        () -> assertThat(result.height()).isEqualTo(book.getBookPhysicalInfo().getHeight()),
        () -> assertThat(result.width()).isEqualTo(book.getBookPhysicalInfo().getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(book.getBookPhysicalInfo().getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(book.getBookPhysicalInfo().getPageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(editParam.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(editParam.purchasePrice()),
        () -> assertThat(result.promotion()).isEqualTo(editParam.promotion()),
        () -> assertThat(result.publisher()).isEqualTo(book.getPublisher()),
        () -> assertThat(result.stockCount()).isEqualTo(editParam.stockCount()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(book.getCoverImgFile().getCoverImgUrl()),
        () ->
            assertThat(result.detailImgUrls())
                .containsExactlyInAnyOrderElementsOf(book.getDetailImgUrls()),
        () ->
            assertThat(result.previewUrls())
                .containsExactlyInAnyOrderElementsOf(book.getPreviewUrls()));
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
                CustomFileFixture.createFile(notAuthorityAuthorId, FileType.COVERS),
                CustomFileFixture.createFiles(notAuthorityAuthorId, FileType.DETAILS),
                CustomFileFixture.createFiles(notAuthorityAuthorId, FileType.PREVIEWS)));
  }
}
