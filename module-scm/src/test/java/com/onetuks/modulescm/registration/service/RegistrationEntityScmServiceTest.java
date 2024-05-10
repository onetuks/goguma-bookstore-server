package com.onetuks.modulescm.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulecommon.file.FileType;
import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.file.FileWrapper.FileWrapperCollection;
import com.onetuks.modulecommon.fixture.FileWrapperFixture;
import com.onetuks.modulecommon.service.S3Repository;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.fixture.RegistrationFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.registration.entity.RegistrationEntity;
import com.onetuks.modulepersistence.registration.repository.RegistrationJpaRepository;
import com.onetuks.modulescm.ScmIntegrationTest;
import com.onetuks.modulescm.registration.service.dto.param.RegistrationCreateParam;
import com.onetuks.modulescm.registration.service.dto.param.RegistrationEditParam;
import com.onetuks.modulescm.registration.service.dto.result.RegistrationInspectionResult;
import com.onetuks.modulescm.registration.service.dto.result.RegistrationResult;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

class RegistrationEntityScmServiceTest extends ScmIntegrationTest {

  @Autowired private RegistrationScmService registrationScmService;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private RegistrationJpaRepository registrationJpaRepository;
  @Autowired private S3Repository s3Repository;
  @Autowired private BookJpaRepository bookJpaRepository;

  private AuthorEntity authorEntity;
  private RegistrationCreateParam createParam;
  private RegistrationEditParam editParam;

  @BeforeEach
  void setUp() {
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    authorEntity = authorJpaRepository.save(AuthorFixture.create(memberEntity));

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
    long authorId = authorEntity.getAuthorId();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(authorId, FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(authorId, FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(authorId, FileType.PREVIEWS);
    FileWrapper sampleFile = FileWrapperFixture.createFile(authorId, FileType.SAMPLES);

    // When
    RegistrationResult result =
        registrationScmService.createRegistration(
            authorEntity.getAuthorId(),
            createParam,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // Then
    File savedCoverImgFile = s3Repository.getFile(coverImgFile.getUri());
    List<File> savedDetailImgFiles =
        detailImgFiles.fileWrappers().stream()
            .map(file -> s3Repository.getFile(file.getUri()))
            .toList();
    List<File> savedPreviewFiles =
        previewFiles.fileWrappers().stream().map(file -> s3Repository.getFile(file.getUri())).toList();
    File savedSampleFile = s3Repository.getFile(sampleFile.getUri());

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
    long authorId = authorEntity.getAuthorId();
    FileWrapper coverImgFile = FileWrapperFixture.createNullFile();
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(authorId, FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(authorId, FileType.PREVIEWS);
    FileWrapper sampleFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                authorEntity.getAuthorId(),
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
    long authorId = authorEntity.getAuthorId();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(authorId, FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(authorId, FileType.PREVIEWS);
    FileWrapper sampleFile = FileWrapperFixture.createNullFile();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                authorEntity.getAuthorId(),
                createParam,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간 등록 요청할 때 상세 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoDetailImgFiles_ExceptionTest() {
    // Given
    long authorId = authorEntity.getAuthorId();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(authorId, FileType.PREVIEWS);
    FileWrapper sampleFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                authorEntity.getAuthorId(),
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
    long authorId = authorEntity.getAuthorId();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(authorId, FileType.DETAILS);
    FileWrapperCollection previewFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapper sampleFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.SAMPLES);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                authorEntity.getAuthorId(),
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
    RegistrationEntity save = registrationJpaRepository.save(RegistrationFixture.create(authorEntity));
    boolean approvalResult = true;
    String approvalMemo = "검수 완료";

    // When
    RegistrationInspectionResult result =
        registrationScmService.updateRegistrationApprovalInfo(
            save.getRegistrationId(), approvalResult, approvalMemo);

    // Then
    boolean existsBook =
        bookJpaRepository.findAll().stream()
            .anyMatch(book -> Objects.equals(book.getIsbn(), save.getIsbn()));

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
    long authorId = authorEntity.getAuthorId();
    RegistrationEntity save = registrationJpaRepository.save(RegistrationFixture.create(authorEntity));
    FileWrapper coverImgFile = FileWrapperFixture.createFile(authorId, FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(authorId, FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(authorId, FileType.PREVIEWS);
    FileWrapper sampleFile = FileWrapperFixture.createFile(authorId, FileType.SAMPLES);

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
    File savedCoverImgFile = s3Repository.getFile(coverImgFile.getUri());
    List<File> savedDetailImgFiles =
        detailImgFiles.fileWrappers().stream()
            .map(file -> s3Repository.getFile(file.getUri()))
            .toList();
    List<File> savedPreviewFiles =
        previewFiles.fileWrappers().stream().map(file -> s3Repository.getFile(file.getUri())).toList();
    File savedSampleFile = s3Repository.getFile(sampleFile.getUri());

    assertAll(
        () -> assertThat(result.registrationId()).isPositive(),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo("신간 등록 검수 중입니다."),
        () -> assertThat(result.title()).isEqualTo(save.getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(editParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editParam.summary()),
        () -> assertThat(result.categories()).isEqualTo(editParam.categories()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.isbn()).isEqualTo(save.getIsbn()),
        () -> assertThat(result.height()).isEqualTo(save.getHeight()),
        () -> assertThat(result.width()).isEqualTo(save.getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(save.getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(save.getPageCount()),
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
    RegistrationEntity save = registrationJpaRepository.save(RegistrationFixture.create(authorEntity));
    FileWrapper coverImgFile = FileWrapperFixture.createNullFile();
    FileWrapperCollection detailImgFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapperCollection previewFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapper sampleFile = FileWrapperFixture.createNullFile();

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
        () -> assertThat(result.title()).isEqualTo(save.getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(editParam.oneLiner()),
        () -> assertThat(result.summary()).isEqualTo(editParam.summary()),
        () -> assertThat(result.categories()).isEqualTo(editParam.categories()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.isbn()).isEqualTo(save.getIsbn()),
        () -> assertThat(result.height()).isEqualTo(save.getHeight()),
        () -> assertThat(result.width()).isEqualTo(save.getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(save.getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(save.getPageCount()),
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
    RegistrationEntity save = registrationJpaRepository.save(RegistrationFixture.create(authorEntity));

    // When
    registrationScmService.deleteRegistration(authorEntity.getAuthorId(), save.getRegistrationId());

    // Then
    boolean result = registrationJpaRepository.existsById(save.getRegistrationId());

    assertThat(result).isFalse();
    assertThrows(NoSuchKeyException.class, () -> s3Repository.getFile(save.getCoverImgUrl()));
  }

  @Test
  @DisplayName("신간등록을 삭제할 권한이 없는 경우 예외가 발생하고, 이미지 파일과 샘플 파일은 삭제되지 않는다.")
  void deleteRegistration_NoAuthority_ExceptionTest() {
    // Given
    long otherAuthorId = 123_412L;
    FileWrapper coverImgFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.COVERS);
    FileWrapperCollection detailImgFiles =
        FileWrapperFixture.createFiles(authorEntity.getAuthorId(), FileType.DETAILS);
    FileWrapperCollection previewFiles =
        FileWrapperFixture.createFiles(authorEntity.getAuthorId(), FileType.PREVIEWS);
    FileWrapper sampleFile = FileWrapperFixture.createFile(authorEntity.getAuthorId(), FileType.SAMPLES);

    RegistrationResult result =
        registrationScmService.createRegistration(
            authorEntity.getAuthorId(),
            createParam,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // When & Then
    assertThrows(
        ApiAccessDeniedException.class,
        () -> registrationScmService.deleteRegistration(otherAuthorId, result.registrationId()));

    File savedCoverImgFile = s3Repository.getFile(coverImgFile.getUri());
    List<File> savedDetailImgFiles =
        detailImgFiles.fileWrappers().stream()
            .map(file -> s3Repository.getFile(file.getUri()))
            .toList();
    List<File> savedPreviewFiles =
        previewFiles.fileWrappers().stream().map(file -> s3Repository.getFile(file.getUri())).toList();
    File savedSampleFile = s3Repository.getFile(sampleFile.getUri());

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
    RegistrationEntity save = registrationJpaRepository.save(RegistrationFixture.create(authorEntity));

    // When
    RegistrationResult result =
        registrationScmService.readRegistration(authorEntity.getAuthorId(), save.getRegistrationId());

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () -> assertThat(result.approvalResult()).isFalse(),
        () -> assertThat(result.approvalMemo()).isEqualTo("유효하지 않은 ISBN입니다."),
        () -> assertThat(result.title()).isEqualTo(save.getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(save.getOneLiner()),
        () -> assertThat(result.summary()).isEqualTo(save.getSummary()),
        () -> assertThat(result.categories()).isEqualTo(save.getCategories()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.isbn()).isEqualTo(save.getIsbn()),
        () -> assertThat(result.height()).isEqualTo(save.getHeight()),
        () -> assertThat(result.width()).isEqualTo(save.getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(save.getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(save.getPageCount()),
        () -> assertThat(result.regularPrice()).isEqualTo(save.getRegularPrice()),
        () -> assertThat(result.purchasePrice()).isEqualTo(save.getPurchasePrice()),
        () -> assertThat(result.stockCount()).isEqualTo(save.getStockCount()),
        () -> assertThat(result.promotion()).isEqualTo(save.isPromotion()));
  }

  @Test
  @DisplayName("신간등록 조회 시 작가 본인이 아니라면 조회할 수 없다.")
  void readRegistration_NotAuthor_ExceptionTest() {
    // Given
    long notAuthorityId = 123_144L;
    RegistrationEntity save = registrationJpaRepository.save(RegistrationFixture.create(authorEntity));

    // When & Then
    assertThrows(
        ApiAccessDeniedException.class,
        () -> registrationScmService.readRegistration(notAuthorityId, save.getRegistrationId()));
  }

  @Test
  @DisplayName("모든 신간등록을 조회한다.")
  void getAllRegistrationTest() {
    // Given
    registrationJpaRepository.save(RegistrationFixture.create(authorEntity));
    registrationJpaRepository.save(RegistrationFixture.create(authorEntity));

    // 새로운 작가에 대한 신간 등록
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    AuthorEntity newAuthorEntity = authorJpaRepository.save(AuthorFixture.create(memberEntity));
    registrationJpaRepository.save(RegistrationFixture.create(newAuthorEntity));

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
    registrationJpaRepository.save(RegistrationFixture.create(authorEntity));
    registrationJpaRepository.save(RegistrationFixture.create(authorEntity));

    // 새로운 작가에 대한 신간 등록
    MemberEntity memberEntity = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    AuthorEntity newAuthorEntity = authorJpaRepository.save(AuthorFixture.create(memberEntity));
    registrationJpaRepository.save(RegistrationFixture.create(newAuthorEntity));

    // When
    Page<RegistrationResult> results =
        registrationScmService.readAllRegistrationsByAuthor(
            authorEntity.getAuthorId(), authorEntity.getAuthorId(), PageRequest.of(0, 10));

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
    registrationJpaRepository.save(RegistrationFixture.create(authorEntity));

    // When & Then
    assertThrows(
        ApiAccessDeniedException.class,
        () ->
            registrationScmService.readAllRegistrationsByAuthor(
                notAuthorityId, authorEntity.getAuthorId(), PageRequest.of(0, 10)));
  }
}
