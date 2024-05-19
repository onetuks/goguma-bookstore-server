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
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.coredomain.AuthorFixture;
import com.onetuks.coredomain.MemberFixture;
import com.onetuks.coredomain.RegistrationFixture;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.vo.BookConceptualInfo;
import com.onetuks.coredomain.book.model.vo.BookPhysicalInfo;
import com.onetuks.coredomain.book.model.vo.BookPriceInfo;
import com.onetuks.coredomain.global.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.global.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.global.file.filepath.PreviewFilePath.PreviewFilePaths;
import com.onetuks.coredomain.global.file.filepath.SampleFilePath;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coredomain.registration.model.vo.ApprovalInfo;
import com.onetuks.coreobj.FileWrapperFixture;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.enums.member.RoleType;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import com.onetuks.coreobj.file.UUIDProvider;
import com.onetuks.scmdomain.ScmDomainIntegrationTest;
import com.onetuks.scmdomain.registration.param.RegistrationCreateParam;
import com.onetuks.scmdomain.registration.param.RegistrationEditParam;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class RegistrationScmServiceTest extends ScmDomainIntegrationTest {

  //  @Autowired private RegistrationScmService registrationScmService;

  //  @MockBean private RegistrationScmRepository registrationScmRepository;
  //  @MockBean private AuthorScmRepository authorScmRepository;
  //  @MockBean private BookScmRepository bookScmRepository;
  //  @MockBean private FileRepository fileRepository;

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
    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(FileType.COVERS, uuid);
    FileWrapperCollection detailImgFiles = FileWrapperFixture.createFiles(FileType.DETAILS, uuid);
    FileWrapperCollection previewFiles = FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid);
    FileWrapper sampleFile = FileWrapperFixture.createFile(FileType.SAMPLES, uuid);
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

    Registration expected =
        new Registration(
            createId(),
            author,
            ApprovalInfo.init(),
            new BookConceptualInfo(
                param.title(),
                param.oneLiner(),
                param.summary(),
                param.categories(),
                param.publisher(),
                param.isbn()),
            new BookPhysicalInfo(
                param.height(), param.width(), param.coverType(), param.pageCount()),
            new BookPriceInfo(
                param.price(), param.salesRate(), param.isPromotion(), param.stockCount()),
            CoverImgFilePath.of(coverImgFile.getUri()),
            DetailImgFilePaths.of(detailImgFiles.getUris()),
            PreviewFilePaths.of(previewFiles.getUris()),
            SampleFilePath.of(sampleFile.getUri()));

    given(authorScmRepository.readByMember(authorMember.memberId())).willReturn(author);
    given(registrationScmRepository.create(any())).willReturn(expected);

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

    verify(fileRepository, atLeastOnce()).putFile(any());
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
    Pageable pageable = PageRequest.of(0, count);
    List<Author> authors =
        IntStream.range(0, 2)
            .mapToObj(
                i ->
                    AuthorFixture.create(
                        createId(), MemberFixture.create(createId(), RoleType.AUTHOR)))
            .toList();
    Page<Registration> registrations =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(i -> RegistrationFixture.create(createId(), authors.get(i % 2), false))
                .toList());

    given(registrationScmRepository.readAll(pageable)).willReturn(registrations);

    // When
    Page<Registration> results = registrationScmService.readAllRegistrations(pageable);

    // Then
    assertThat(results).hasSize(count);
  }

  @Test
  @DisplayName("작가별 신간등록을 조회한다.")
  void readAllRegistrationsByAuthorTest() {
    // Given
    int count = 10;
    Pageable pageable = PageRequest.of(0, count);
    Page<Registration> registrations =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(i -> RegistrationFixture.create(createId(), author, false))
                .toList());

    given(memberRepository.read(authorMember.memberId())).willReturn(authorMember);
    given(authorScmRepository.read(author.authorId())).willReturn(author);
    given(registrationScmRepository.readAll(author.authorId(), pageable)).willReturn(registrations);

    // When
    Page<Registration> results =
        registrationScmService.readAllRegistrationsByAuthor(
            authorMember.memberId(), author.authorId(), pageable);

    // Then
    assertThat(results).hasSize(count).containsExactlyInAnyOrderElementsOf(registrations);
  }

  @Test
  @DisplayName("작가별 신간등록 조회 시 작가 본인이 아니라면 조회할 수 없다.")
  void readAllRegistrationsByAuthor_NotAuthor_ExceptionTest() {
    // Given
    Member notAuthMember = MemberFixture.create(createId(), RoleType.AUTHOR);
    Pageable pageable = PageRequest.of(0, 10);

    given(memberRepository.read(notAuthMember.memberId())).willReturn(notAuthMember);
    given(authorScmRepository.read(author.authorId())).willReturn(author);

    // When & Then
    assertThrows(
        ApiAccessDeniedException.class,
        () ->
            registrationScmService.readAllRegistrationsByAuthor(
                notAuthMember.memberId(), author.authorId(), pageable));
  }

  @Test
  @DisplayName("신간등록을 검수 승인하면 검수 결과가 변경되고, 도서 등록이 완료된다.")
  void updateRegistrationApprovalInfo_TrueTest() {
    // Given
    Registration before = RegistrationFixture.create(createId(), author, false);
    boolean isApproved = true;
    Registration after = before.changeApprovalInfo(isApproved, null);

    given(registrationScmRepository.read(before.registrationId())).willReturn(before);
    given(registrationScmRepository.update(any())).willReturn(after);

    // When
    Registration result =
        registrationScmService.updateRegistrationApprovalInfo(
            before.registrationId(), isApproved, null);

    // Then
    assertAll(
        () -> assertThat(result.approvalInfo().isApproved()).isTrue(),
        () -> assertThat(result.approvalInfo().approvalMemo()).isEqualTo(ApprovalInfo.APPROVED));

    verify(bookScmRepository, times(1)).create(after);
  }

  @Test
  @DisplayName("신간등록을 검수 승인하면 검수 결과가 변경되고, 도서 등록이 완료된다.")
  void updateRegistrationApprovalInfo_FalseTest() {
    // Given
    Registration before = RegistrationFixture.create(createId(), author, false);
    boolean isApproved = false;
    Registration after = before.changeApprovalInfo(isApproved, null);

    given(registrationScmRepository.read(before.registrationId())).willReturn(before);
    given(registrationScmRepository.update(any())).willReturn(after);

    // When
    Registration result =
        registrationScmService.updateRegistrationApprovalInfo(
            before.registrationId(), isApproved, null);

    // Then
    assertAll(
        () -> assertThat(result.approvalInfo().isApproved()).isFalse(),
        () -> assertThat(result.approvalInfo().approvalMemo()).isEqualTo(ApprovalInfo.REJECTED));

    verify(bookScmRepository, times(0)).create(any());
  }

  @Test
  @DisplayName("신간등록을 수정한다. 커버 이미지 파일과 샘플 파일이 S3에 저장된다.")
  void updateRegistrationTest() {
    // Given
    String uuid = UUIDProvider.provideUUID();
    FileWrapper coverImgFile = FileWrapperFixture.createFile(FileType.COVERS, uuid);
    FileWrapperCollection detailImgFiles = FileWrapperFixture.createFiles(FileType.DETAILS, uuid);
    FileWrapperCollection previewFiles = FileWrapperFixture.createFiles(FileType.PREVIEWS, uuid);
    FileWrapper sampleFile = FileWrapperFixture.createFile(FileType.SAMPLES, uuid);
    RegistrationEditParam param =
        new RegistrationEditParam(
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());
    Registration before = RegistrationFixture.create(createId(), author, false);
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
            PreviewFilePaths.of(previewFiles.getUris()),
            SampleFilePath.of(sampleFile.getUri()));

    given(registrationScmRepository.read(before.registrationId())).willReturn(before);
    given(registrationScmRepository.update(any())).willReturn(after);

    // When
    Registration result =
        registrationScmService.updateRegistration(
            author.member().memberId(),
            before.registrationId(),
            param,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // Then
    assertAll(
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

    verify(fileRepository, atLeastOnce()).putFile(any());
  }

  @Test
  @DisplayName("신간등록을 수정할 때 사용자가 제공하지 않은 이미지 정보는 기존 데이터를 유지한다.")
  void updateRegistration_NoFiles_Test() {
    // Given
    FileWrapper coverImgFile = FileWrapperFixture.createNullFile();
    FileWrapperCollection detailImgFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapperCollection previewFiles = new FileWrapperCollection(Collections.emptyList());
    FileWrapper sampleFile = FileWrapperFixture.createNullFile();
    RegistrationEditParam param =
        new RegistrationEditParam(
            createOneLiner(),
            createSummary(),
            createCategories(),
            createPrice(),
            createSalesRate(),
            createPromotion(),
            createStockCount());
    Registration before = RegistrationFixture.create(createId(), author, false);
    Registration after =
        before.changeRegistration(
            param.oneLiner(),
            param.summary(),
            param.categories(),
            param.price(),
            param.salesRate(),
            param.isPromotion(),
            param.stockCount(),
            before.coverImgFilePath(),
            before.detailImgFilePaths(),
            before.previewFilePaths(),
            before.sampleFilePath());

    given(registrationScmRepository.read(before.registrationId())).willReturn(before);
    given(registrationScmRepository.update(any())).willReturn(after);

    // When
    Registration result =
        registrationScmService.updateRegistration(
            author.member().memberId(),
            before.registrationId(),
            param,
            coverImgFile,
            detailImgFiles,
            previewFiles,
            sampleFile);

    // Then
    assertAll(
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
                .isEqualTo(before.coverImgFilePath().getUri()),
        () ->
            assertThat(result.detailImgFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(before.detailImgFilePaths().getUris()),
        () ->
            assertThat(result.previewFilePaths().getUris())
                .containsExactlyInAnyOrderElementsOf(before.previewFilePaths().getUris()),
        () ->
            assertThat(result.sampleFilePath().getUri())
                .isEqualTo(before.sampleFilePath().getUri()));

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
    verify(fileRepository, atLeastOnce()).deleteFile(any());
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
