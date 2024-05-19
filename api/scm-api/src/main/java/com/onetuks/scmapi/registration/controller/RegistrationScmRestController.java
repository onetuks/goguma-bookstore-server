package com.onetuks.scmapi.registration.controller;

import com.onetuks.coreauth.util.admin.AdminId;
import com.onetuks.coreauth.util.author.AuthorId;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.FileWrapper.FileWrapperCollection;
import com.onetuks.coreobj.file.UUIDProvider;
import com.onetuks.scmapi.registration.dto.request.RegistrationPatchJudgeRequest;
import com.onetuks.scmapi.registration.dto.request.RegistrationPatchRequest;
import com.onetuks.scmapi.registration.dto.request.RegistrationPostRequest;
import com.onetuks.scmapi.registration.dto.response.RegistrationIsbnGetResponse;
import com.onetuks.scmapi.registration.dto.response.RegistrationPatchJudgeResponse;
import com.onetuks.scmapi.registration.dto.response.RegistrationResponse;
import com.onetuks.scmapi.registration.dto.response.RegistrationResponse.RegistrationResponses;
import com.onetuks.scmdomain.registration.service.RegistrationScmService;
import com.onetuks.scmdomain.verification.webclient.dto.result.RegistrationIsbnResult;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/scm/registrations")
public class RegistrationScmRestController {

  private final RegistrationScmService registrationScmService;

  public RegistrationScmRestController(RegistrationScmService registrationScmService) {
    this.registrationScmService = registrationScmService;
  }

  /**
   * 신간 등록
   *
   * @param authorMemberId : 로그인한 작가 아이디
   * @param request : 신간 등록 요청 정보
   * @param coverImgFile : 신간 표지 이미지 파일
   * @param detailImgFiles : 신간 상세 이미지 파일
   * @param previewFiles : 신간 미리보기 이미지 파일
   * @param sampleFile : 신간 샘플 파일
   * @return 200 OK
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponse> postNewRegistration(
      @AuthorId Long authorMemberId,
      @RequestBody @Valid RegistrationPostRequest request,
      @RequestPart(name = "cover-img-file") MultipartFile coverImgFile,
      @RequestPart(name = "detail-img-files") MultipartFile[] detailImgFiles,
      @RequestPart(name = "preview-files") MultipartFile[] previewFiles,
      @RequestPart(name = "sample-file") MultipartFile sampleFile) {
    String uuid = UUIDProvider.provideUUID();
    Registration result =
        registrationScmService.createRegistration(
            authorMemberId,
            request.to(),
            FileWrapper.of(FileType.COVERS, uuid, coverImgFile),
            FileWrapperCollection.of(FileType.DETAILS, uuid, detailImgFiles),
            FileWrapperCollection.of(FileType.PREVIEWS, uuid, previewFiles),
            FileWrapper.of(FileType.SAMPLES, uuid, sampleFile));
    RegistrationResponse response = RegistrationResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 승인 - 관리자용
   *
   * @param adminId : 로그인한 관리자 아이디
   * @param registrationId : 신간 등록 아이디
   * @param request : 신간 등록 승인 요청 정보
   * @return 200 OK
   */
  @PatchMapping(
      path = "/{registrationId}/judge",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationPatchJudgeResponse> patchRegistrationJudgement(
      @AdminId Long adminId,
      @PathVariable(name = "registrationId") Long registrationId,
      @RequestBody @Valid RegistrationPatchJudgeRequest request) {
    Registration result =
        registrationScmService.updateRegistrationApprovalInfo(
            registrationId, request.approvalResult(), request.approvalMemo());
    RegistrationPatchJudgeResponse response = RegistrationPatchJudgeResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 수정 - 작가용
   *
   * @param authorMemberId : 로그인한 작가 아이디
   * @param registrationId : 신간 등록 아이디
   * @param request : 신간 등록 수정 요청 정보
   * @param coverImgFile : 신간 표지 이미지 파일
   * @param detailImgFiles : 신간 상세 이미지 파일
   * @param previewFiles : 신간 미리보기 이미지 파일
   * @param sampleFile : 신간 샘플 파일
   * @return 200 OK
   */
  @PatchMapping(
      path = "/{registrationId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponse> patchRegistration(
      @AuthorId Long authorMemberId,
      @PathVariable(name = "registrationId") Long registrationId,
      @RequestBody @Valid RegistrationPatchRequest request,
      @RequestPart(name = "cover-img-file", required = false) MultipartFile coverImgFile,
      @RequestPart(name = "detail-img-files", required = false) MultipartFile[] detailImgFiles,
      @RequestPart(name = "preview-files", required = false) MultipartFile[] previewFiles,
      @RequestPart(name = "sample-file", required = false) MultipartFile sampleFile) {
    String uuid = UUIDProvider.provideUUID();
    Registration result =
        registrationScmService.updateRegistration(
            authorMemberId,
            registrationId,
            request.to(),
            FileWrapper.of(FileType.COVERS, uuid, coverImgFile),
            FileWrapperCollection.of(FileType.DETAILS, uuid, detailImgFiles),
            FileWrapperCollection.of(FileType.PREVIEWS, uuid, previewFiles),
            FileWrapper.of(FileType.SAMPLES, uuid, sampleFile));
    RegistrationResponse response = RegistrationResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 삭제 - 작가용
   *
   * @param authorMemberId : 로그인한 작가 아이디
   * @param registrationId : 신간 등록 아이디
   * @return 204 NO_CONTENT
   */
  @DeleteMapping(path = "/{registrationId}")
  public ResponseEntity<Void> removeRegistration(
      @AuthorId Long authorMemberId, @PathVariable(name = "registrationId") Long registrationId) {
    registrationScmService.deleteRegistration(authorMemberId, registrationId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 신간 등록 조회 - 작가용
   *
   * @param authorMemberId : 로그인한 작가 아이디
   * @param registrationId : 신간 등록 아이디
   * @return 200 OK
   */
  @GetMapping(path = "/{registrationId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponse> getRegistration(
      @AuthorId Long authorMemberId, @PathVariable(name = "registrationId") Long registrationId) {
    Registration result = registrationScmService.readRegistration(authorMemberId, registrationId);
    RegistrationResponse response = RegistrationResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 신간 등록 전체 조회 - 관리자용
   *
   * @param adminId : 로그인한 관리자 아이디
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponses> getAllRegistrations(
      @AdminId Long adminId,
      @PageableDefault(sort = "registrationId", direction = Direction.DESC) Pageable pageable) {
    Page<Registration> result = registrationScmService.readAllRegistrations(pageable);
    RegistrationResponses response = RegistrationResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가별 신간 등록 조회 - 작가용
   *
   * @param authorMemberId : 로그인한 작가 아이디
   * @param authorId : 작가 아이디
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationResponses> getAllRegistrationsByAuthor(
      @AuthorId Long authorMemberId,
      @RequestParam(name = "authorId") Long authorId,
      @PageableDefault(sort = "registrationId", direction = Direction.DESC) Pageable pageable) {
    Page<Registration> result =
        registrationScmService.readAllRegistrationsByAuthor(authorMemberId, authorId, pageable);
    RegistrationResponses response = RegistrationResponses.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * ISBN 검증
   *
   * @param isbn : ISBN
   * @return 200 OK
   */
  @GetMapping(path = "/isbn/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegistrationIsbnGetResponse> getBookInfoByIsbn(
      @PathVariable(name = "isbn") String isbn) {
    RegistrationIsbnResult result = registrationScmService.verifyIsbn(isbn);
    RegistrationIsbnGetResponse response = RegistrationIsbnGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
