package com.onetuks.goguma_bookstore.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.model.vo.PageSizeInfo;
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
import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationInspectionParam;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationEditResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationInspectionResult;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class RegistrationServiceTest extends IntegrationTest {

  @Autowired private RegistrationService registrationService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private RegistrationJpaRepository registrationJpaRepository;
  @Autowired private S3Service s3Service;

  private Author author;
  private RegistrationCreateParam param;

  @BeforeEach
  void setUp() {
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    author = authorJpaRepository.save(AuthorFixture.create(member));

    param =
        new RegistrationCreateParam(
            "신간 제목",
            "한줄 요약",
            "줄거리",
            List.of(Category.NOVEL, Category.ESSEY),
            "출판사",
            "1234567890123",
            new PageSizeInfo(200, 100),
            "양장본",
            500L,
            10000,
            100,
            true);
  }

  @Test
  @DisplayName("신간 등록을 요청하면 신간이 등록되고, 커버 이미지, 목업 이미지, 미리보기 파일, 샘플 파일이 S3에 저장된다.")
  void createRegistrationTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createFile(authorId, FileType.COVERS);
    List<CustomFile> mockUpFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(authorId, FileType.SAMPLES);

    // When
    RegistrationCreateResult result =
        registrationService.createRegistration(
            author.getAuthorId(), param, coverImgFile, mockUpFiles, previewFiles, sampleFile);

    // Then
    File savedCoverImgFile = s3Service.getFile(coverImgFile.getUri());
    List<File> savedMockUpFiles =
        mockUpFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    List<File> savedPreviewFiles =
        previewFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    File savedSampleFile = s3Service.getFile(sampleFile.getUri());

    assertAll(
        () -> assertThat(result.registrationId()).isPositive(),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo("신간 등록 검수 중입니다."),
        () -> assertThat(result.title()).isEqualTo(param.title()),
        () -> assertThat(result.oneLiner()).isEqualTo(param.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(param.summary()),
        () -> assertThat(result.categories()).isEqualTo(param.categories()),
        () -> assertThat(result.publisher()).isEqualTo(param.publisher()),
        () -> assertThat(result.isbn()).isEqualTo(param.isbn()),
        () -> assertThat(result.pageSizeInfo()).isEqualTo(param.pageSizeInfo()),
        () -> assertThat(result.coverType()).isEqualTo(param.coverType()),
        () -> assertThat(result.pageCount()).isEqualTo(param.pageCount()),
        () -> assertThat(result.price()).isEqualTo(param.price()),
        () -> assertThat(result.stockCount()).isEqualTo(param.stockCount()),
        () -> assertThat(result.promotion()).isEqualTo(param.promotion()),
        () -> assertThat(savedCoverImgFile).exists(),
        () -> assertThat(savedMockUpFiles).hasSize(10),
        () -> assertThat(savedPreviewFiles).hasSize(25),
        () -> assertThat(savedSampleFile).exists());
  }

  @Test
  @DisplayName("신간 등록을 요청할 때 커버 이미지 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoCoverImgFile_ExceptionTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createNullFile();
    List<CustomFile> mockUpFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationService.createRegistration(
                author.getAuthorId(), param, coverImgFile, mockUpFiles, previewFiles, sampleFile));
  }

  @Test
  @DisplayName("신간 등록을 요청할 때 샘플 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoSampleFile_ExceptionTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> mockUpFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createNullFile();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationService.createRegistration(
                author.getAuthorId(), param, coverImgFile, mockUpFiles, previewFiles, sampleFile));
  }

  @Test
  @DisplayName("신간 등록 요청할 때 목업 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoMockUpFiles_ExceptionTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> mockUpFiles = List.of();
    List<CustomFile> previewFiles = CustomFileFixture.createFiles(authorId, FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationService.createRegistration(
                author.getAuthorId(), param, coverImgFile, mockUpFiles, previewFiles, sampleFile));
  }

  @Test
  @DisplayName("신간 등록 요청할 때 미리보기 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoPreviewFiles_ExceptionTest() {
    // Given
    long authorId = author.getAuthorId();
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    List<CustomFile> mockUpFiles = CustomFileFixture.createFiles(authorId, FileType.DETAILS);
    List<CustomFile> previewFiles = List.of();
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationService.createRegistration(
                author.getAuthorId(), param, coverImgFile, mockUpFiles, previewFiles, sampleFile));
  }

  @Test
  @DisplayName("신간등록을 검수한다.")
  void updateRegistrationApprovalTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));
    RegistrationInspectionParam param = new RegistrationInspectionParam(true, "검수 완료");

    // When
    RegistrationInspectionResult result =
        registrationService.updateRegistrationApproval(save.getRegistrationId(), param);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () -> assertThat(result.approvalResult()).isTrue(),
        () -> assertThat(result.approvalMemo()).isEqualTo(param.approvalMemo()));
  }

  @Test
  @DisplayName("신간등록을 수정한다. 커버 이미지 파일과 샘플 파일이 S3에 저장된다.")
  void updateRegistrationTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));
    RegistrationEditParam param =
        new RegistrationEditParam(
            "신간 제목 수정", "신간 요약 수정", 20000, 200, "1234567890123", "출판사 수정", false);
    CustomFile coverImgFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.COVERS);
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    // When
    RegistrationEditResult result =
        registrationService.updateRegistration(
            save.getRegistrationId(), param, coverImgFile, sampleFile);

    // Then
    File savedCoverImgFile = s3Service.getFile(coverImgFile.getUri());
    File savedSampleFile = s3Service.getFile(sampleFile.getUri());

    assertAll(
        () -> assertThat(savedCoverImgFile).hasSize(coverImgFile.getMultipartFile().getSize()),
        () -> assertThat(savedSampleFile).hasSize(sampleFile.getMultipartFile().getSize()),
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () ->
            assertThat(result.coverImgUrl())
                .isEqualTo(coverImgFile.toCoverImgFile().getCoverImgUrl()),
        () -> assertThat(result.title()).isEqualTo(param.title()),
        () -> assertThat(result.summary()).isEqualTo(param.summary()),
        () -> assertThat(result.price()).isEqualTo(param.price()),
        () -> assertThat(result.stockCount()).isEqualTo(param.stockCount()),
        () -> assertThat(result.isbn()).isEqualTo(param.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(param.publisher()),
        () -> assertThat(result.promotion()).isEqualTo(param.promotion()),
        () -> assertThat(result.sampleUrl()).isEqualTo(sampleFile.toSampleFile().getSampleUrl()));
  }

  @Test
  @DisplayName("신간등록을 삭제한다. 커버 이미지 파일과 샘플 파일이 S3에서 삭제된다.")
  void deleteRegistrationTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    registrationService.deleteRegistration(author.getAuthorId(), save.getRegistrationId());

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
    List<CustomFile> mockUpFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), FileType.DETAILS);
    List<CustomFile> previewFiles =
        CustomFileFixture.createFiles(author.getAuthorId(), FileType.PREVIEWS);
    CustomFile sampleFile = CustomFileFixture.createFile(author.getAuthorId(), FileType.SAMPLES);

    RegistrationCreateResult result =
        registrationService.createRegistration(
            author.getAuthorId(), param, coverImgFile, mockUpFiles, previewFiles, sampleFile);

    // When & Then
    assertThrows(
        AccessDeniedException.class,
        () -> registrationService.deleteRegistration(otherAuthorId, result.registrationId()));

    File savedCoverImgFile = s3Service.getFile(coverImgFile.getUri());
    List<File> savedMockUpfiles =
        mockUpFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    List<File> savedPreviewFiles =
        previewFiles.stream().map(file -> s3Service.getFile(file.getUri())).toList();
    File savedSampleFile = s3Service.getFile(sampleFile.getUri());

    assertAll(
        () -> assertThat(savedCoverImgFile).exists(),
        () -> assertThat(savedMockUpfiles).hasSize(10),
        () -> assertThat(savedPreviewFiles).hasSize(25),
        () -> assertThat(savedSampleFile).exists());
  }

  @Test
  @DisplayName("신간등록 단건 조회한다.")
  void getRegistrationTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    RegistrationGetResult result =
        registrationService.getRegistration(author.getAuthorId(), save.getRegistrationId());

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(save.getCoverImgFile().getCoverImgUrl()),
        () -> assertThat(result.title()).isEqualTo(save.getTitle()),
        () -> assertThat(result.summary()).isEqualTo(save.getSummary()),
        () -> assertThat(result.price()).isEqualTo(save.getPrice()),
        () -> assertThat(result.stockCount()).isEqualTo(save.getStockCount()),
        () -> assertThat(result.isbn()).isEqualTo(save.getIsbn()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.promotion()).isEqualTo(save.getPromotion()),
        () -> assertThat(result.sampleUrl()).isEqualTo(save.getSampleFile().getSampleUrl()));
  }

  @Test
  @DisplayName("신간등록 조회 시 작가 본인이 아니라면 조회할 수 없다.")
  void getRegistration_NotAuthor_ExceptionTest() {
    // Given
    long notAuthorityId = 123_144L;
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When & Then
    assertThrows(
        AccessDeniedException.class,
        () -> registrationService.getRegistration(notAuthorityId, save.getRegistrationId()));
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
    List<RegistrationGetResult> results = registrationService.getAllRegistrations();

    // Then
    assertThat(results)
        .hasSize(3)
        .allSatisfy(
            result -> {
              assertThat(result.registrationId()).isPositive();
            });
  }

  @Test
  @DisplayName("작가별 신간등록을 조회한다.")
  void getAllRegistrationsByAuthorTest() {
    // Given
    registrationJpaRepository.save(RegistrationFixture.create(author));
    registrationJpaRepository.save(RegistrationFixture.create(author));

    // 새로운 작가에 대한 신간 등록
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author newAuthor = authorJpaRepository.save(AuthorFixture.create(member));
    registrationJpaRepository.save(RegistrationFixture.create(newAuthor));

    // When
    List<RegistrationGetResult> results =
        registrationService.getAllRegistrationsByAuthor(author.getAuthorId(), author.getAuthorId());

    // Then
    assertThat(results).hasSize(2);
  }

  @Test
  @DisplayName("작가별 신간등록 조회 시 작가 본인이 아니라면 조회할 수 없다.")
  void getAllRegistrationsByAuthor_NotAuthor_ExceptionTest() {
    // Given
    long notAuthorityId = 123_144L;
    registrationJpaRepository.save(RegistrationFixture.create(author));

    // When & Then
    assertThrows(
        AccessDeniedException.class,
        () ->
            registrationService.getAllRegistrationsByAuthor(notAuthorityId, author.getAuthorId()));
  }
}
