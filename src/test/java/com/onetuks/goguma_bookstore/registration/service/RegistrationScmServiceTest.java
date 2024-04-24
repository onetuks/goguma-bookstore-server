package com.onetuks.goguma_bookstore.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.book.vo.Category;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.CustomFileFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.RegistrationFixture;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationEditParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationResult;
import java.io.File;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class RegistrationScmServiceTest extends IntegrationTest {

  @Autowired private RegistrationScmService registrationScmService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private RegistrationJpaRepository registrationJpaRepository;
  @Autowired private S3Service s3Service;
  @Autowired private BookJpaRepository bookJpaRepository;

  private Author author;
  private RegistrationCreateParam createParam;
  private RegistrationEditParam editParam;

  @BeforeEach
  void setUp() {
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    author = authorJpaRepository.save(AuthorFixture.create(member));

    createParam =
        new RegistrationCreateParam(
            "신간 제목",
            "한줄 요약",
            "줄거리",
            List.of(Category.NOVEL, Category.ESSEY),
            "1234567890123",
            200,
            100,
            "양장본",
            500L,
            20_000,
            10_000,
            true,
            "출판사A",
            100);
    editParam =
        new RegistrationEditParam(
            "한줄 요약", "줄거리", List.of(Category.NOVEL, Category.ESSEY), 20_000, 10_000, true, 100);
  }

  @Test
  @DisplayName("신간 등록을 요청하면 신간이 등록되고, 커버 이미지, 목업 이미지, 미리보기 파일, 샘플 파일이 S3에 저장된다.")
  void createRegistrationTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createFile(authorId, FileType.COVERS);
    List<CustomFile> detailImgFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(authorId, FileType.SAMPLES);

    // When
    RegistrationResult result =
        registrationScmService.createRegistration(
            author.getAuthorId(),
            createParam,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // Then
    File savedCoverImgFile = s3Service.getFile(coverImgFile.getUri());
    List<File> savedDetailImgFiles =
        detailImgFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    List<File> savedPreviewFiles =
        previewFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    File savedSampleFile = s3Service.getFile(sampleFile.getUri());

    assertAll(
        () -> assertThat(result.registrationId()).isPositive(),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo("신간 등록 검수 중입니다."),
        () -> assertThat(result.title()).isEqualTo(createParam.title()),
        () -> assertThat(result.oneLiner()).isEqualTo(createParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(createParam.summary()),
        () -> assertThat(result.categories()).isEqualTo(createParam.categories()),
        () -> assertThat(result.publisher()).isEqualTo(createParam.publisher()),
        () -> assertThat(result.isbn()).isEqualTo(createParam.isbn()),
        () -> assertThat(result.height()).isEqualTo(createParam.height()),
        () -> assertThat(result.width()).isEqualTo(createParam.width()),
        () -> assertThat(result.coverType()).isEqualTo(createParam.coverType()),
        () -> assertThat(result.pageCount()).isEqualTo(createParam.pageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(createParam.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(createParam.purchasePrice()),
        () -> assertThat(result.stockCount()).isEqualTo(createParam.stockCount()),
        () -> assertThat(result.promotion()).isEqualTo(createParam.promotion()),
        () -> assertThat(savedCoverImgFile).exists(),
        () -> assertThat(savedDetailImgFiles).hasSize(10),
        () -> assertThat(savedPreviewFiles).hasSize(25),
        () -> assertThat(savedSampleFile).exists());
  }

  @Test
  @DisplayName("신간 등록을 요청할 때 커버 이미지 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoCoverImgFile_ExceptionTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createNullFile();
    List<CustomFile> detailImgFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                author.getAuthorId(),
                createParam,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간 등록을 요청할 때 샘플 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoSampleFile_ExceptionTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> detailImgFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createNullFile();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                author.getAuthorId(),
                createParam,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간 등록 요청할 때 목업 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoMockUpFiles_ExceptionTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> detailImgFiles = List.of();
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                author.getAuthorId(),
                createParam,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간 등록 요청할 때 미리보기 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoPreviewFiles_ExceptionTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> detailImgFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = List.of();
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                author.getAuthorId(),
                createParam,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간등록을 검수 승인하면 검수 결과가 변경되고, 도서 등록이 완료된다.")
  void updateRegistrationApprovalInfoTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));
    boolean approvalResult = true;
    String approvalMemo = "검수 완료";

    // When
    RegistrationInspectionResult result =
        registrationScmService.updateRegistrationApprovalInfo(
            save.getRegistrationId(), approvalResult, approvalMemo);

    // Then
    boolean existsBook =
        bookJpaRepository.findAll().stream()
            .anyMatch(
                book ->
                    Objects.equals(
                        book.getBookConceptualInfo().getIsbn(),
                        save.getBookConceptualInfo().getIsbn()));

    assertThat(existsBook).isTrue();
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(approvalResult),
        () -> assertThat(result.approvalMemo()).isEqualTo(approvalMemo));
  }

  @Test
  @DisplayName("신간등록을 수정한다. 커버 이미지 파일과 샘플 파일이 S3에 저장된다.")
  void updateRegistrationTest() {
    // Given
    long authorId = author.getAuthorId();
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));
    CustomFile coverImgFile = CustomFileFixture.createFile(authorId, FileType.COVERS);
    List<CustomFile> detailImgFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(authorId, FileType.SAMPLES);

    // When
    RegistrationResult result =
        registrationScmService.updateRegistration(
            save.getRegistrationId(),
            editParam,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // Then
    File savedCoverImgFile = s3Service.getFile(coverImgFile.getUri());
    List<File> savedDetailImgFiles =
        detailImgFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    List<File> savedPreviewFiles =
        previewFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    File savedSampleFile = s3Service.getFile(sampleFile.getUri());

    assertAll(
        () -> assertThat(result.registrationId()).isPositive(),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo("신간 등록 검수 중입니다."),
        () -> assertThat(result.title()).isEqualTo(save.getBookConceptualInfo().getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(editParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editParam.summary()),
        () -> assertThat(result.categories()).isEqualTo(editParam.categories()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.isbn()).isEqualTo(save.getBookConceptualInfo().getIsbn()),
        () -> assertThat(result.height()).isEqualTo(save.getBookPhysicalInfo().getHeight()),
        () -> assertThat(result.width()).isEqualTo(save.getBookPhysicalInfo().getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(save.getBookPhysicalInfo().getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(save.getBookPhysicalInfo().getPageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(editParam.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(editParam.purchasePrice()),
        () -> assertThat(result.stockCount()).isEqualTo(editParam.stockCount()),
        () -> assertThat(result.promotion()).isEqualTo(editParam.promotion()),
        () -> assertThat(savedCoverImgFile).exists(),
        () -> assertThat(savedDetailImgFiles).hasSize(10),
        () -> assertThat(savedPreviewFiles).hasSize(25),
        () -> assertThat(savedSampleFile).exists());
  }

  @Test
  @DisplayName("신간등록을 수정할 때 사용자가 제공하지 않은 이미지 정보는 기존 데이터를 유지한다.")
  void updateRegistration_NoFiles_Test() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));
    CustomFile coverImgFile = CustomFileFixture.createNullFile();
    List<CustomFile> detailImgFiles = List.of();
    List<CustomFile> previewFiles = List.of();
    CustomFile sampleFile = CustomFileFixture.createNullFile();

    // When
    RegistrationResult result =
        registrationScmService.updateRegistration(
            save.getRegistrationId(),
            editParam,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isPositive(),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo("신간 등록 검수 중입니다."),
        () -> assertThat(result.title()).isEqualTo(save.getBookConceptualInfo().getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(editParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editParam.summary()),
        () -> assertThat(result.categories()).isEqualTo(editParam.categories()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.isbn()).isEqualTo(save.getBookConceptualInfo().getIsbn()),
        () -> assertThat(result.height()).isEqualTo(save.getBookPhysicalInfo().getHeight()),
        () -> assertThat(result.width()).isEqualTo(save.getBookPhysicalInfo().getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(save.getBookPhysicalInfo().getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(save.getBookPhysicalInfo().getPageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(editParam.regularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(editParam.purchasePrice()),
        () -> assertThat(result.stockCount()).isEqualTo(editParam.stockCount()),
        () -> assertThat(result.promotion()).isEqualTo(editParam.promotion()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(save.getCoverImgUrl()),
        () ->
            assertThat(result.detailImgUrls())
                .containsExactlyInAnyOrderElementsOf(save.getDetailImgUrls()),
        () ->
            assertThat(result.previewUrls())
                .containsExactlyInAnyOrderElementsOf(save.getPreviewUrls()),
        () -> assertThat(result.sampleUrl()).isEqualTo(save.getSampleUrl()));
  }

  @Test
  @DisplayName("신간등록을 삭제한다. 커버 이미지 파일과 샘플 파일이 S3에서 삭제된다.")
  void deleteRegistrationTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    registrationScmService.deleteRegistration(author.getAuthorId(), save.getRegistrationId());

    // Then
    boolean result = registrationJpaRepository.existsById(save.getRegistrationId());

    assertThat(result).isFalse();
    assertThrows(
        NoSuchKeyException.class, () -> s3Service.getFile(save.getCoverImgFile().getCoverImgUri()));
  }

  @Test
  @DisplayName("신간등록을 삭제할 권한이 없는 경우 예외가 발생하고, 이미지 파일과 샘플 파일은 삭제되지 않는다.")
  void deleteRegistration_NoAuthority_ExceptionTest() {
    // Given
    long otherAuthorId = 123_412L;
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> detailImgFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), FileType.DETAILS);
    List<CustomFile> previewFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    RegistrationResult result =
        registrationScmService.createRegistration(
            author.getAuthorId(),
            createParam,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // When & Then
    assertThrows(
        AccessDeniedException.class,
        () -> registrationScmService.deleteRegistration(otherAuthorId, result.registrationId()));

    File savedCoverImgFile = s3Service.getFile(coverImgFile.getUri());
    List<File> savedDetailImgFiles =
        detailImgFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    List<File> savedPreviewFiles =
        previewFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    File savedSampleFile = s3Service.getFile(sampleFile.getUri());

    assertAll(
        () -> assertThat(savedCoverImgFile).exists(),
        () -> assertThat(savedDetailImgFiles).hasSize(10),
        () -> assertThat(savedPreviewFiles).hasSize(25),
        () -> assertThat(savedSampleFile).exists());
  }

  @Test
  @DisplayName("신간등록 단건 조회한다.")
  void readRegistrationTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    RegistrationResult result =
        registrationScmService.readRegistration(author.getAuthorId(), save.getRegistrationId());

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo("유효하지 않은 ISBN입니다."),
        () -> assertThat(result.title()).isEqualTo(save.getBookConceptualInfo().getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(save.getBookConceptualInfo().getOneLiner()),
        () -> assertThat(result.summary()).isEqualTo(save.getBookConceptualInfo().getSummary()),
        () ->
            assertThat(result.categories()).isEqualTo(save.getBookConceptualInfo().getCategories()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.isbn()).isEqualTo(save.getBookConceptualInfo().getIsbn()),
        () -> assertThat(result.height()).isEqualTo(save.getBookPhysicalInfo().getHeight()),
        () -> assertThat(result.width()).isEqualTo(save.getBookPhysicalInfo().getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(save.getBookPhysicalInfo().getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(save.getBookPhysicalInfo().getPageCount()),
        () ->
            assertThat(result.regularPrice()).isEqualTo(save.getBookPriceInfo().getRegularPrice()),
        () ->
            assertThat(result.purchasePrice())
                .isEqualTo(save.getBookPriceInfo().getPurchasePrice()),
        () -> assertThat(result.stockCount()).isEqualTo(save.getStockCount()),
        () -> assertThat(result.promotion()).isEqualTo(save.getBookPriceInfo().getPromotion()));
  }

  @Test
  @DisplayName("신간등록 조회 시 작가 본인이 아니라면 조회할 수 없다.")
  void readRegistration_NotAuthor_ExceptionTest() {
    // Given
    long notAuthorityId = 123_144L;
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When & Then
    assertThrows(
        AccessDeniedException.class,
        () -> registrationScmService.readRegistration(notAuthorityId, save.getRegistrationId()));
  }

  @Test
  @DisplayName("모든 신간등록을 조회한다.")
  void getAllRegistrationTest() {
    // Given
    registrationJpaRepository.save(RegistrationFixture.create(author));
    registrationJpaRepository.save(RegistrationFixture.create(author));

    // 새로운 작가에 대한 신간 등록
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author newAuthor = authorJpaRepository.save(AuthorFixture.create(member));
    registrationJpaRepository.save(RegistrationFixture.create(newAuthor));

    // When
    Page<RegistrationResult> results =
        registrationScmService.readAllRegistrations(PageRequest.of(0, 10));

    // Then
    assertThat(results)
        .hasSize(3)
        .allSatisfy(result -> assertThat(result.registrationId()).isPositive());
  }

  @Test
  @DisplayName("작가별 신간등록을 조회한다.")
  void readAllRegistrationsByAuthorTest() {
    // Given
    registrationJpaRepository.save(RegistrationFixture.create(author));
    registrationJpaRepository.save(RegistrationFixture.create(author));

    // 새로운 작가에 대한 신간 등록
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author newAuthor = authorJpaRepository.save(AuthorFixture.create(member));
    registrationJpaRepository.save(RegistrationFixture.create(newAuthor));

    // When
    Page<RegistrationResult> results =
        registrationScmService.readAllRegistrationsByAuthor(
            author.getAuthorId(), author.getAuthorId(), PageRequest.of(0, 10));

    // Then
    assertAll(
        () -> assertThat(results.getTotalElements()).isEqualTo(2),
        () -> assertThat(results.getTotalPages()).isEqualTo(1),
        () -> assertThat(results.getContent()).hasSize(2));
  }

  @Test
  @DisplayName("작가별 신간등록 조회 시 작가 본인이 아니라면 조회할 수 없다.")
  void readAllRegistrationsByAuthor_NotAuthor_ExceptionTest() {
    // Given
    long notAuthorityId = 123_144L;
    registrationJpaRepository.save(RegistrationFixture.create(author));

    // When & Then
    assertThrows(
        AccessDeniedException.class,
        () ->
            registrationScmService.readAllRegistrationsByAuthor(
                notAuthorityId, author.getAuthorId(), PageRequest.of(0, 10)));
  }
}
