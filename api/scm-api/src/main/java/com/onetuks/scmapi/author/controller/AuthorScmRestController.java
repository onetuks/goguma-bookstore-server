package com.onetuks.scmapi.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.coreauth.util.admin.AdminId;
import com.onetuks.coreauth.util.author.AuthorId;
import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coreobj.enums.file.FileType;
import com.onetuks.coreobj.file.FileWrapper;
import com.onetuks.coreobj.file.UUIDProvider;
import com.onetuks.scmapi.author.dto.request.AuthorCreateRequest;
import com.onetuks.scmapi.author.dto.response.AuthorCreateResponse;
import com.onetuks.scmapi.author.dto.response.AuthorEditJudgeResponse;
import com.onetuks.scmapi.author.dto.request.AuthorEditRequest;
import com.onetuks.scmapi.author.dto.response.AuthorEditResponse;
import com.onetuks.scmapi.author.dto.response.AuthorScmDetailsResponse;
import com.onetuks.scmapi.author.dto.response.AuthorScmDetailsResponse.AuthorScmDetailsResponses;
import com.onetuks.scmdomain.author.service.AuthorScmService;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/scm/authors")
public class AuthorScmRestController {

  private final AuthorScmService authorScmService;

  public AuthorScmRestController(AuthorScmService authorScmService) {
    this.authorScmService = authorScmService;
  }

  /**
   * 작가 등록 요청
   * @param memberId : 로그인한 멤버 ID
   * @param request : 작가 등록 요청 내용
   * @return 200 OK
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorCreateResponse> requestEnrollment(
      @MemberId Long memberId, @RequestBody @Valid AuthorCreateRequest request) {
    Author result = authorScmService.createAuthor(memberId, request.to());
    AuthorCreateResponse response = AuthorCreateResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 작가 등록 상세 조회
   * @param authorMemberId : 로그인한 작가 ID
   * @param authorId : 작가 등록 식별자
   * @return 200 OK
   */
  @GetMapping(
      path = "/{authorId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorScmDetailsResponse> getAuthorEnrollmentDetails(
      @AuthorId Long authorMemberId,
      @PathVariable(name = "authorId") Long authorId) {
    Author result = authorScmService.readAuthorDetails(authorMemberId, authorId);
    AuthorScmDetailsResponse response = AuthorScmDetailsResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가 등록 전체 조회
   * @param adminId : 관리자 확인용
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorScmDetailsResponses> getAllAuthorEnrollmentDetails(
      @AdminId Long adminId,
      @PageableDefault(sort = "enrollmentAt", direction = Direction.DESC) Pageable pageable) {
    Page<Author> results =
        authorScmService.readAllAuthorDetails(pageable);
    AuthorScmDetailsResponses responses = AuthorScmDetailsResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 작가 등록 심사
   * @param adminId : 관리자 확인용
   * @param authorId : 심사할 작가 ID
   * @return 200 OK
   */
  @PatchMapping(
      path = "/{authorId}/judge",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEditJudgeResponse> judgeEnrollment(
      @AdminId Long adminId, @PathVariable(name = "authorId") Long authorId) {
    Author result = authorScmService.updateAuthorEnrollmentPassed(authorId);
    AuthorEditJudgeResponse response = AuthorEditJudgeResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가 프로필 수정
   * @param authorMemberId : 로그인한 작가 ID
   * @param authorId : 수정할 작가 ID
   * @param request : 수정할 내용
   * @param profileImgFile : 프로필 이미지 파일
   * @return 200 OK
   */
  @PatchMapping(
      path = "/{authorId}",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEditResponse> editAuthorProfile(
      @AuthorId Long authorMemberId,
      @PathVariable(name = "authorId") Long authorId,
      @RequestBody @Valid AuthorEditRequest request,
      @RequestPart(name = "profile-img-file", required = false) MultipartFile profileImgFile) {
    Author result =
        authorScmService.updateAuthorProfile(
            authorMemberId, authorId, request.to(),
            FileWrapper.of(FileType.PROFILES, UUIDProvider.provideUUID(), profileImgFile));
    AuthorEditResponse response = AuthorEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }


  /**
   * 작가 등록 취소
   * @param authorMemberId : 본인 확인용
   * @return 204 No Content
   */
  @DeleteMapping
  public ResponseEntity<Void> cancelAuthorEnrollment(@AuthorId Long authorMemberId) {
    authorScmService.deleteAuthor(authorMemberId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
