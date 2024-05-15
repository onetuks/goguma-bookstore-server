package com.onetuks.scmdomain.service;

import static com.onetuks.coredomain.util.TestValueProvider.createCategories;
import static com.onetuks.coredomain.util.TestValueProvider.createCoverType;
import static com.onetuks.coredomain.util.TestValueProvider.createHeight;
import static com.onetuks.coredomain.util.TestValueProvider.createId;
import static com.onetuks.coredomain.util.TestValueProvider.createIsbn;
import static com.onetuks.coredomain.util.TestValueProvider.createOneLiner;
import static com.onetuks.coredomain.util.TestValueProvider.createPageCount;
import static com.onetuks.coredomain.util.TestValueProvider.createPrice;
import static com.onetuks.coredomain.util.TestValueProvider.createPromotion;
import static com.onetuks.coredomain.util.TestValueProvider.createPublisher;
import static com.onetuks.coredomain.util.TestValueProvider.createSalesRate;
import static com.onetuks.coredomain.util.TestValueProvider.createStockCount;
import static com.onetuks.coredomain.util.TestValueProvider.createSummary;
import static com.onetuks.coredomain.util.TestValueProvider.createTitle;
import static com.onetuks.coredomain.util.TestValueProvider.createWidth;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.BookFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RegistrationFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.author.repository.AuthorScmRepository;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.repository.BookScmRepository;
import com.onetuks.coredomain.file.repository.FileRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coredomain.registration.model.vo.ApprovalInfo;
import com.onetuks.coredomain.registration.repository.RegistrationScmRepository;
import com.onetuks.coreobj.FileWrapperFixture;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.coreobj.vo.FileWrapper;
import com.onetuks.coreobj.vo.FileWrapper.FileWrapperCollection;
import com.onetuks.coreobj.vo.UUIDProvider;
import com.onetuks.scmdomain.ScmDomainIntegrationTest;
import com.onetuks.scmdomain.registration.param.RegistrationCreateParam;
import com.onetuks.scmdomain.registration.param.RegistrationEditParam;
import com.onetuks.scmdomain.registration.service.RegistrationScmService;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class RegistrationScmServiceTest extends ScmDomainIntegrationTest {

  @Autowired private RegistrationScmService registrationScmService;

  @MockBean private RegistrationScmRepository registrationScmRepository;
  @MockBean private AuthorScmRepository authorScmRepository;
  @MockBean private BookScmRepository bookScmRepository;
  @MockBean private FileRepository fileRepository;

  private Member authorMember;
  private Author author;

  @BeforeEach
  void setUp() {
    authorMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    author = AuthorFixture.create(createId(), authorMember);
  }

  @Test
  @DisplayName("신간 등록을 요청하면 신간이 등록되고, 커버 이미지, 목업 이미지, 미리보기 파일, 샘플 파일이 S3에 저장된다.")
  void createRegistrationTest() {
    // Given
    RegistrationCreateParam param =
        new RegistrationCreateParam(
            createTitle(),
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPublisher(),
            createIsbn(),
            createHeight(),
            createWidth(),
            createCoverType(),
            createPageCount(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());

    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(FileType.COVERS, uuid);
    FileWrapperCollection detailImgFiles = FileWrapperFixture.createFiles(FileType.DETAILS, uuid);
    FileWrapperCollection previewFiles = FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid);
    FileWrapper sampleFile = FileWrapperFixture.createFile(FileType.SAMPLES, uuid);

    Registration registration = RegistrationFixture.create(createId(), author, false);

    given(authorScmRepository.readByMember(authorMember.memberId())).willReturn(author);
    given(registrationScmRepository.create(any())).willReturn(registration);

    // When
    Registration result =
        registrationScmService.createRegistration(
            authorMember.memberId(), param, coverImgFile, detailImgFiles, previewFiles, sampleFile);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isPositive(),
        () -> assertThat(result.approvalInfo().isApproved()).isFalse(),
        () -> assertThat(result.approvalInfo().approvalMemo()).isEqualTo("신간 등록 검수 중입니다."),
        () -> assertThat(result.bookConceptualInfo().title()).isEqualTo(param.title()),
        () -> assertThat(result.bookConceptualInfo().oneLiner()).isEqualTo(param.oneLiner()),
        () -> assertThat(result.bookConceptualInfo().summary()).isEqualTo(param.summary()),
        () -> assertThat(result.bookConceptualInfo().categories()).isEqualTo(param.categories()),
        () -> assertThat(result.bookConceptualInfo().publisher()).isEqualTo(param.publisher()),
        () -> assertThat(result.bookConceptualInfo().isbn()).isEqualTo(param.isbn()),
        () -> assertThat(result.bookPhysicalInfo().height()).isEqualTo(param.height()),
        () -> assertThat(result.bookPhysicalInfo().width()).isEqualTo(param.width()),
        () -> assertThat(result.bookPhysicalInfo().coverType()).isEqualTo(param.coverType()),
        () -> assertThat(result.bookPhysicalInfo().pageCount()).isEqualTo(param.pageCount()),
        () -> assertThat(result.bookPriceInfo().price()).isEqualTo(param.price()),
        () -> assertThat(result.bookPriceInfo().salesRate()).isEqualTo(param.salesRate()),
        () -> assertThat(result.bookPriceInfo().isPromotion()).isEqualTo(param.isPromotion()),
        () -> assertThat(result.bookPriceInfo().stockCount()).isEqualTo(param.stockCount()),
        () -> assertThat(result.coverImgFilePath().getUri()).isEqualTo(coverImgFile.getUri()),
        () ->
            assertThat(result.detailImgFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(detailImgFiles.getUris()),
        () ->
            assertThat(result.previewFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(previewFiles.getUris()),
        () -> assertThat(result.sampleFilePath().getUri()).isEqualTo(sampleFile.getUri()));

    verify(fileRepository).putFile(any());
  }

  @Test
  @DisplayName("신간 등록을 요청할 때 커버 이미지 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoCoverImgFile_ExceptionTest() {
    // Given
    RegistrationCreateParam param =
        new RegistrationCreateParam(
            createTitle(),
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPublisher(),
            createIsbn(),
            createHeight(),
            createWidth(),
            createCoverType(),
            createPageCount(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());

    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createNullFile();
    FileWrapperCollection detailImgFiles = FileWrapperFixture.createFiles(FileType.DETAILS, uuid);
    FileWrapperCollection previewFiles = FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid);
    FileWrapper sampleFile = FileWrapperFixture.createFile(FileType.SAMPLES, uuid);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                authorMember.memberId(),
                param,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간 등록 요청할 때 상세 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoDetailImgFiles_ExceptionTest() {
    // Given
    RegistrationCreateParam param =
        new RegistrationCreateParam(
            createTitle(),
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPublisher(),
            createIsbn(),
            createHeight(),
            createWidth(),
            createCoverType(),
            createPageCount(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());

    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(FileType.COVERS, uuid);
    FileWrapperCollection detailImgFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapperCollection previewFiles = FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid);
    FileWrapper sampleFile = FileWrapperFixture.createFile(FileType.SAMPLES, uuid);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                authorMember.memberId(),
                param,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간 등록 요청할 때 미리보기 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoPreviewFiles_ExceptionTest() {
    // Given
    RegistrationCreateParam param =
        new RegistrationCreateParam(
            createTitle(),
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPublisher(),
            createIsbn(),
            createHeight(),
            createWidth(),
            createCoverType(),
            createPageCount(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());

    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(FileType.COVERS, uuid);
    FileWrapperCollection detailImgFiles = FileWrapperFixture.createFiles(FileType.DETAILS, uuid);
    FileWrapperCollection previewFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapper sampleFile = FileWrapperFixture.createFile(FileType.SAMPLES, uuid);

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                authorMember.memberId(),
                param,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간 등록을 요청할 때 샘플 파일이 없으면 예외가 발생한다.")
  void createRegistration_NoSampleFile_ExceptionTest() {
    // Given
    RegistrationCreateParam param =
        new RegistrationCreateParam(
            createTitle(),
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPublisher(),
            createIsbn(),
            createHeight(),
            createWidth(),
            createCoverType(),
            createPageCount(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());

    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(FileType.COVERS, uuid);
    FileWrapperCollection detailImgFiles = FileWrapperFixture.createFiles(FileType.DETAILS, uuid);
    FileWrapperCollection previewFiles = FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid);
    FileWrapper sampleFile = FileWrapperFixture.createNullFile();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () ->
            registrationScmService.createRegistration(
                authorMember.memberId(),
                param,
                coverImgFile,
                detailImgFiles,
                previewFiles,
                sampleFile));
  }

  @Test
  @DisplayName("신간등록 단건 조회한다.")
  void readRegistrationTest() {
    // Given
    Registration registration = RegistrationFixture.create(createId(), author, false);

    given(registrationScmRepository.read(registration.registrationId())).willReturn(registration);

    // When
    Registration result =
        registrationScmService.readRegistration(author.authorId(), registration.registrationId());

    // Then
    assertAll(
        () -> assertThat(result).isEqualTo(registration),
        () -> assertThat(result.approvalInfo().isApproved()).isFalse(),
        () -> assertThat(result.approvalInfo().approvalMemo()).isEqualTo("신간 등록 검수 중입니다."),
        () ->
            assertThat(result.bookConceptualInfo().title())
                .isEqualTo(registration.bookConceptualInfo().title()),
        () ->
            assertThat(result.bookConceptualInfo().oneLiner())
                .isEqualTo(registration.bookConceptualInfo().oneLiner()),
        () ->
            assertThat(result.bookConceptualInfo().summary())
                .isEqualTo(registration.bookConceptualInfo().summary()),
        () ->
            assertThat(result.bookConceptualInfo().categories())
                .isEqualTo(registration.bookConceptualInfo().categories()),
        () ->
            assertThat(result.bookConceptualInfo().publisher())
                .isEqualTo(registration.bookConceptualInfo().publisher()),
        () ->
            assertThat(result.bookConceptualInfo().isbn())
                .isEqualTo(registration.bookConceptualInfo().isbn()),
        () ->
            assertThat(result.bookPhysicalInfo().height())
                .isEqualTo(registration.bookPhysicalInfo().height()),
        () ->
            assertThat(result.bookPhysicalInfo().width())
                .isEqualTo(registration.bookPhysicalInfo().width()),
        () ->
            assertThat(result.bookPhysicalInfo().coverType())
                .isEqualTo(registration.bookPhysicalInfo().coverType()),
        () ->
            assertThat(result.bookPhysicalInfo().pageCount())
                .isEqualTo(registration.bookPhysicalInfo().pageCount()),
        () ->
            assertThat(result.bookPriceInfo().price())
                .isEqualTo(registration.bookPriceInfo().price()),
        () ->
            assertThat(result.bookPriceInfo().salesRate())
                .isEqualTo(registration.bookPriceInfo().salesRate()),
        () ->
            assertThat(result.bookPriceInfo().isPromotion())
                .isEqualTo(registration.bookPriceInfo().isPromotion()),
        () ->
            assertThat(result.bookPriceInfo().stockCount())
                .isEqualTo(registration.bookPriceInfo().stockCount()),
        () -> assertThat(result.coverImgFilePath()).isNotNull(),
        () -> assertThat(result.detailImgFilePaths().getUris()).isNotEmpty(),
        () -> assertThat(result.previewFilePaths().getUris()).isNotEmpty(),
        () -> assertThat(result.sampleFilePath()).isNotNull());
  }

  @Test
  @DisplayName("신간등록 조회 시 작가 본인이 아니라면 조회할 수 없다.")
  void readRegistration_NotAuthor_ExceptionTest() {
    // Given
    long notExistsAuthorId = 123_303_212L;
    Registration registration = RegistrationFixture.create(createId(), author, false);

    given(registrationScmRepository.read(registration.registrationId())).willReturn(registration);

    // When & Then
    assertThrows(
        ApiAccessDeniedException.class,
        () ->
            registrationScmService.readRegistration(
                notExistsAuthorId, registration.registrationId()));
  }

  @Test
  @DisplayName("모든 신간등록을 조회한다.")
  void readAllRegistrationsTest() {
    // Given
    int count = 10;
    List<Author> authors =
        IntStream.range(0, 2)
            .mapToObj(
                i ->
                    AuthorFixture.create(
                        createId(), MemberFixture.create(createId(), RoleType.AUTHOR)))
            .toList();
    List<Registration> registrations =
        IntStream.range(0, count)
            .mapToObj(i -> RegistrationFixture.create(createId(), authors.get(i % 2), false))
            .toList();

    given(registrationScmRepository.readAll()).willReturn(registrations);

    // When
    List<Registration> results = registrationScmService.readAllRegistrations();

    // Then
    assertThat(results).hasSize(count);
  }

  @Test
  @DisplayName("작가별 신간등록을 조회한다.")
  void readAllRegistrationsByAuthorTest() {
    // Given
    int count = 10;
    List<Registration> registrations =
        IntStream.range(0, count)
            .mapToObj(i -> RegistrationFixture.create(createId(), author, false))
            .toList();

    given(authorScmRepository.read(author.authorId())).willReturn(author);
    given(registrationScmRepository.readAll()).willReturn(registrations);

    // When
    List<Registration> results =
        registrationScmService.readAllRegistrationsByAuthor(
            authorMember.memberId(), author.authorId());

    // Then
    assertThat(results).hasSize(count).containsExactlyInAnyOrderElementsOf(registrations);
  }

  @Test
  @DisplayName("작가별 신간등록 조회 시 작가 본인이 아니라면 조회할 수 없다.")
  void readAllRegistrationsByAuthor_NotAuthor_ExceptionTest() {
    // Given
    long userMemberId = 123_144L;

    given(authorScmRepository.read(author.authorId())).willReturn(author);

    // When & Then
    assertThrows(
        ApiAccessDeniedException.class,
        () -> registrationScmService.readAllRegistrationsByAuthor(userMemberId, author.authorId()));
  }

  @Test
  @DisplayName("신간등록을 검수 승인하면 검수 결과가 변경되고, 도서 등록이 완료된다.")
  void updateRegistrationApprovalInfo_TrueTest() {
    // Given
    Registration before = RegistrationFixture.create(createId(), author, false);
    Registration after = RegistrationFixture.create(before.registrationId(), author, true);
    boolean isApproved = true;
    Book book = BookFixture.create(createId(), author);

    given(registrationScmRepository.update(any())).willReturn(after);
    given(bookScmRepository.create(any())).willReturn(book);

    // When
    Registration result =
        registrationScmService.updateRegistrationApprovalInfo(
            before.registrationId(), isApproved, null);

    // Then
    assertAll(
        () -> assertThat(result).isEqualTo(after),
        () -> assertThat(result.approvalInfo().isApproved()).isTrue(),
        () -> assertThat(result.approvalInfo().approvalMemo()).isEqualTo(ApprovalInfo.APPROVED),
        () ->
            assertThat(result.bookConceptualInfo().title())
                .isEqualTo(book.bookConceptualInfo().title()));

    verify(bookScmRepository, times(1)).create(after);
  }

  @Test
  @DisplayName("신간등록을 검수 승인하면 검수 결과가 변경되고, 도서 등록이 완료된다.")
  void updateRegistrationApprovalInfo_FalseTest() {
    // Given
    Registration before = RegistrationFixture.create(createId(), author, false);
    boolean isApproved = false;

    given(registrationScmRepository.update(any())).willReturn(before);

    // When
    Registration result =
        registrationScmService.updateRegistrationApprovalInfo(
            before.registrationId(), isApproved, null);

    // Then
    assertAll(
        () -> assertThat(result).isEqualTo(before),
        () -> assertThat(result.approvalInfo().isApproved()).isFalse(),
        () -> assertThat(result.approvalInfo().approvalMemo()).isEqualTo(ApprovalInfo.REJECTED));

    verify(bookScmRepository, times(0)).create(any());
  }

  @Test
  @DisplayName("신간등록을 수정한다. 커버 이미지 파일과 샘플 파일이 S3에 저장된다.")
  void updateRegistrationTest() {
    // Given
    Registration registration = RegistrationFixture.create(createId(), author, false);
    RegistrationEditParam param =
        new RegistrationEditParam(
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());
    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(FileType.COVERS, uuid);
    FileWrapperCollection detailImgFiles = FileWrapperFixture.createFiles(FileType.DETAILS, uuid);
    FileWrapperCollection previewFiles = FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid);
    FileWrapper sampleFile = FileWrapperFixture.createFile(FileType.SAMPLES, uuid);

    // When
    Registration result =
        registrationScmService.updateRegistration(
            registration.registrationId(),
            param,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // Then
    assertAll(
        () -> assertThat(result).isEqualTo(registration),
        () -> assertThat(result.approvalInfo().isApproved()).isFalse(),
        () ->
            assertThat(result.approvalInfo().approvalMemo()).isEqualTo(ApprovalInfo.WAIT_APPROVAL),
        () -> assertThat(result.bookConceptualInfo().oneLiner()).isEqualTo(param.oneLiner()),
        () -> assertThat(result.bookConceptualInfo().summary()).isEqualTo(param.summary()),
        () ->
            assertThat(result.bookConceptualInfo().categories())
                .containsExactlyInAnyOrderElementsOf(param.categories()),
        () -> assertThat(result.bookPriceInfo().price()).isEqualTo(param.price()),
        () -> assertThat(result.bookPriceInfo().salesRate()).isEqualTo(param.salesRate()),
        () -> assertThat(result.bookPriceInfo().isPromotion()).isEqualTo(param.isPromotion()),
        () -> assertThat(result.bookPriceInfo().stockCount()).isEqualTo(param.stockCount()),
        () -> assertThat(result.coverImgFilePath().getUri()).isEqualTo(coverImgFile.getUri()),
        () ->
            assertThat(result.detailImgFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(detailImgFiles.getUris()),
        () ->
            assertThat(result.previewFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(previewFiles.getUris()),
        () -> assertThat(result.sampleFilePath().getUri()).isEqualTo(sampleFile.getUri()));

    verify(fileRepository).putFile(any());
  }

  @Test
  @DisplayName("신간등록을 수정할 때 사용자가 제공하지 않은 이미지 정보는 기존 데이터를 유지한다.")
  void updateRegistration_NoFiles_Test() {
    // Given
    Registration registration = RegistrationFixture.create(createId(), author, false);
    RegistrationEditParam param =
        new RegistrationEditParam(
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
    FileWrapper sampleFile = FileWrapperFixture.createNullFile();

    // When
    Registration result =
        registrationScmService.updateRegistration(
            registration.registrationId(),
            param,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // Then
    assertAll(
        () -> assertThat(result).isEqualTo(registration),
        () -> assertThat(result.approvalInfo().isApproved()).isFalse(),
        () ->
            assertThat(result.approvalInfo().approvalMemo()).isEqualTo(ApprovalInfo.WAIT_APPROVAL),
        () -> assertThat(result.bookConceptualInfo().oneLiner()).isEqualTo(param.oneLiner()),
        () -> assertThat(result.bookConceptualInfo().summary()).isEqualTo(param.summary()),
        () ->
            assertThat(result.bookConceptualInfo().categories())
                .containsExactlyInAnyOrderElementsOf(param.categories()),
        () -> assertThat(result.bookPriceInfo().price()).isEqualTo(param.price()),
        () -> assertThat(result.bookPriceInfo().salesRate()).isEqualTo(param.salesRate()),
        () -> assertThat(result.bookPriceInfo().isPromotion()).isEqualTo(param.isPromotion()),
        () -> assertThat(result.bookPriceInfo().stockCount()).isEqualTo(param.stockCount()),
        () ->
            assertThat(result.coverImgFilePath().getUri())
                .isEqualTo(registration.coverImgFilePath().getUri()),
        () ->
            assertThat(result.detailImgFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(registration.detailImgFilePaths().getUris()),
        () ->
            assertThat(result.previewFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(registration.previewFilePaths().getUris()),
        () ->
            assertThat(result.sampleFilePath().getUri())
                .isEqualTo(registration.sampleFilePath().getUri()));

    verify(fileRepository, times(0)).putFile(any());
  }

  @Test
  @DisplayName("신간등록을 삭제한다. 커버 이미지 파일과 샘플 파일이 S3에서 삭제된다.")
  void deleteRegistrationTest() {
    // Given
    Registration registration = RegistrationFixture.create(createId(), author, false);

    given(registrationScmRepository.read(registration.registrationId())).willReturn(registration);

    // When
    registrationScmService.deleteRegistration(
        authorMember.memberId(), registration.registrationId());

    // Then
    verify(fileRepository).deleteFile(any());
    verify(registrationScmRepository, times(1)).delete(registration.registrationId());
  }

  @Test
  @DisplayName("신간등록을 삭제할 권한이 없는 경우 예외가 발생하고, 이미지 파일과 샘플 파일은 삭제되지 않는다.")
  void deleteRegistration_NoAuthority_ExceptionTest() {
    // Given
    long notAuthorizedMemberId = 412_123_142L;
    Registration registration = RegistrationFixture.create(createId(), author, false);

    given(registrationScmRepository.read(registration.registrationId())).willReturn(registration);

    // When & Then
    assertThatThrownBy(
            () ->
                registrationScmService.deleteRegistration(
                    notAuthorizedMemberId, registration.registrationId()))
        .isInstanceOf(ApiAccessDeniedException.class);
  }
}
