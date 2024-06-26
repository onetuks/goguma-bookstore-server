package com.onetuks.scmdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createCategories;
import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static com.onetuks.coredomain.util.TestValueProvider.createOneLiner;
import static com.onetuks.coredomain.util.TestValueProvider.createPrice;
import static com.onetuks.coredomain.util.TestValueProvider.createPromotion;
import static com.onetuks.coredomain.util.TestValueProvider.createSalesRate;
import static com.onetuks.coredomain.util.TestValueProvider.createStockCount;
import static com.onetuks.coredomain.util.TestValueProvider.createSummary;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.BookFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RegistrationFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.global.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.global.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.global.file.filepath.PreviewFilePath.PreviewFilePaths;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coreobj.FileWrapperFixture;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import com.onetuks.coreobj.file.UUIDProvider;
import com.onetuks.scmdomain.ScmDomainIntegrationTest;
import com.onetuks.scmdomain.book.param.BookEditParam;
import java.util.Collections;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class BookScmServiceTest extends ScmDomainIntegrationTest {

  //  @Autowired private BookScmService bookScmService;

  //  @MockBean private BookScmRepository bookScmRepository;
  //  @MockBean private AuthorScmRepository authorScmRepository;
  //  @MockBean private RegistrationScmRepository registrationScmRepository;
  //  @MockBean private FileRepository fileRepository;

  private Member authorMember;
  private Author author;

  @BeforeEach
  void setUp() {
    authorMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    author = AuthorFixture.create(createId(), authorMember);
  }

  @Test
  @DisplayName("작가의 모든 도서를 조회한다.")
  void readAllBooksByAuthorTest() {
    // Given
    int count = 5;
    Pageable pageable = PageRequest.of(0, 10);
    Page<Book> books =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(i -> BookFixture.create(createId(), author))
                .toList());

    given(authorScmRepository.read(author.authorId())).willReturn(author);
    given(memberRepository.read(authorMember.memberId())).willReturn(authorMember);
    given(bookScmRepository.readAll(author.authorId(), pageable)).willReturn(books);

    // When
    Page<Book> results =
        bookScmService.readAllBooksByAuthor(authorMember.memberId(), author.authorId(), pageable);

    // Then
    assertThat(results).hasSize(count);
  }

  @Test
  @DisplayName("조회하려는 작가와 로그인한 작가가 다르면 시스템 도서 조회 시 예외를 던진다.")
  void readAllBooksByAuthor_NotAuthorityAuthor_ExceptionTest() {
    // Given
    Member notAuthMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    Pageable pageable = PageRequest.of(0, 10);

    given(authorScmRepository.read(author.authorId())).willReturn(author);
    given(memberRepository.read(notAuthMember.memberId())).willReturn(notAuthMember);

    // When
    assertThrows(
        ApiAccessDeniedException.class,
        () ->
            bookScmService.readAllBooksByAuthor(
                notAuthMember.memberId(), author.authorId(), pageable));
  }

  @Test
  @DisplayName("관리자는 작가의 모든 도서를 조회할 수 있다.")
  void readAllBooksByAuthor_Admin_Test() {
    // Given
    int count = 5;
    Member adminMember = MemberFixture.create(createId(), RoleType.ADMIN);
    Pageable pageable = PageRequest.of(0, 10);
    Page<Book> books =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(i -> BookFixture.create(createId(), author))
                .toList());

    given(authorScmRepository.read(author.authorId())).willReturn(author);
    given(memberRepository.read(adminMember.memberId())).willReturn(adminMember);
    given(bookScmRepository.readAll(author.authorId(), pageable)).willReturn(books);

    // When
    Page<Book> results =
        bookScmService.readAllBooksByAuthor(adminMember.memberId(), author.authorId(), pageable);

    // Then
    assertThat(results).hasSize(count);
  }

  @Test
  @DisplayName("도서 정보 수정 시 책 재고량이 0이 되고, 해당 신간등록이 검수 대기인 상태로 변경되며 신간등록 정보도 수정된다.")
  void updateBookTest() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(FileType.COVERS, uuid);
    FileWrapperCollection detailImgFiles = FileWrapperFixture.createFiles(FileType.DETAILS, uuid);
    FileWrapperCollection previewFiles = FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid);
    Book book = BookFixture.create(createId(), author);
    BookEditParam param =
        new BookEditParam(
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());
    Registration before = RegistrationFixture.create(createId(), author, true);
    Registration after =
        before.changeRegistration(
            param.oneLiner(),
            param.summary(),
            param.categories(),
            param.price(),
            param.salesRate(),
            param.isPromotion(),
            param.stockCount(),
            CoverImgFilePath.of(coverImgFile.getUri()),
            DetailImgFilePaths.of(detailImgFiles.getUris()),
            PreviewFilePaths.of(previewFiles.getUris()));

    given(authorScmRepository.readByMember(authorMember.memberId())).willReturn(author);
    given(bookScmRepository.read(book.bookId())).willReturn(book);
    given(registrationScmRepository.readByIsbn(book.bookConceptualInfo().isbn()))
        .willReturn(before);
    given(registrationScmRepository.update(any())).willReturn(after);
    given(bookScmRepository.update(any())).willReturn(book.changeStockCount(0));

    // When
    Book result =
        bookScmService.updateBook(
            authorMember.memberId(),
            book.bookId(),
            param,
            coverImgFile,
            detailImgFiles,
            previewFiles);

    // Then
    assertAll(
        () -> assertThat(result.bookPriceInfo().stockCount()).isZero(),
        () -> assertThat(after.bookConceptualInfo().oneLiner()).isEqualTo(param.oneLiner()),
        () -> assertThat(after.bookPriceInfo().price()).isEqualTo(param.price()),
        () -> assertThat(after.coverImgFilePath().getUri()).isEqualTo(coverImgFile.getUri()),
        () ->
            assertThat(after.detailImgFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(detailImgFiles.getUris()),
        () ->
            assertThat(after.previewFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(previewFiles.getUris()));

    verify(fileRepository, atLeastOnce()).deleteFile(any());
    verify(fileRepository, atLeastOnce()).putFile(any());
  }

  @Test
  @DisplayName("이미지 데이터 없이도 도서 정보 수정 시 책 재고량이 0이 되고, 해당 신간등록이 검수 대기인 상태로 변경되며 신간등록 정보도 수정된다.")
  void updateBook_NoFiles_Test() {
    // Given
    Book before = BookFixture.create(createId(), author);
    Book after = BookFixture.create(before.bookId(), author).changeStockCount(0);
    Registration registration = RegistrationFixture.create(createId(), author, true);
    BookEditParam param =
        new BookEditParam(
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());
    FileWrapper coverImgFile = FileWrapperFixture.createNullFile();
    FileWrapperCollection detailImgFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapperCollection previewFiles = new FileWrapperCollection(Collections.emptyList());

    given(authorScmRepository.readByMember(authorMember.memberId())).willReturn(author);
    given(bookScmRepository.read(before.bookId())).willReturn(before);
    given(registrationScmRepository.readByIsbn(before.bookConceptualInfo().isbn()))
        .willReturn(registration);
    given(bookScmRepository.update(any())).willReturn(before.changeStockCount(0));

    // When
    Book result =
        bookScmService.updateBook(
            authorMember.memberId(),
            before.bookId(),
            param,
            coverImgFile,
            detailImgFiles,
            previewFiles);

    // Then
    assertAll(
        () -> assertThat(result.bookPriceInfo().stockCount()).isZero(),
        () ->
            assertThat(result.bookConceptualInfo().title())
                .isEqualTo(before.bookConceptualInfo().title()),
        () -> assertThat(result.bookPriceInfo().price()).isEqualTo(before.bookPriceInfo().price()),
        () -> assertThat(result.coverImgFilePath()).isEqualTo(before.coverImgFilePath()),
        () -> assertThat(result.detailImgFilePaths()).isEqualTo(before.detailImgFilePaths()),
        () -> assertThat(result.previewFilePaths()).isEqualTo(before.previewFilePaths()));

    verify(fileRepository, times(0)).deleteFile(registration.coverImgFilePath().getUrl());
    verify(fileRepository, times(0)).deleteFile(any());
    verify(fileRepository, times(0)).deleteFile(any());
    verify(fileRepository, times(0)).putFile(coverImgFile);
  }

  @Test
  @DisplayName("도서에 대한 권한이 없는 작가가 도서 수정을 시도하면 예외를 던진다.")
  void updateBook_NotAuthorityAuthor_ExceptionTest() {
    // Given
    Member otherAuthorMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    Author notAuthorityAuthor = AuthorFixture.create(createId(), otherAuthorMember);
    Book book = BookFixture.create(createId(), author);
    BookEditParam param =
        new BookEditParam(
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());
    String uuid = UUIDProvider.provideUUID();

    given(authorScmRepository.readByMember(otherAuthorMember.memberId()))
        .willReturn(notAuthorityAuthor);
    given(bookScmRepository.read(book.bookId())).willReturn(book);

    // When & Then
    assertThrows(
        ApiAccessDeniedException.class,
        () ->
            bookScmService.updateBook(
                otherAuthorMember.memberId(),
                book.bookId(),
                param,
                FileWrapperFixture.createFile(FileType.COVERS, uuid),
                FileWrapperFixture.createFiles(FileType.DETAILS, uuid),
                FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid)));
  }
}
