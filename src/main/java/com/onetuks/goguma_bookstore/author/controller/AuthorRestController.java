package com.onetuks.goguma_bookstore.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.auth.util.author.AuthorId;
import com.onetuks.goguma_bookstore.author.controller.dto.request.AuthorEditRequest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorDetailsResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorDetailsResponse.AuthorDetailsResponses;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorEditResponse;
import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import com.onetuks.goguma_bookstore.global.vo.file.FileType;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/authors")
public class AuthorRestController {

  private final AuthorService authorService;

  public AuthorRestController(AuthorService authorService) {
    this.authorService = authorService;
  }

  /**
   * 작가 프로필 수정
   *
   * @param loginAuthorId : 로그인한 작가 ID
   * @param authorId : 수정할 작가 ID
   * @param request : 작가 프로필 수정 내용
   * @param profileImg : 작가 프로필 수정 이미지 (같은 이미지여도 덮어쓰기)
   * @return authorId, profileImgUrl, nickname, introduction
   */
  @PatchMapping(
      path = "/{authorId}",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorEditResponse> editAuthorProfile(
      @AuthorId Long loginAuthorId,
      @PathVariable(name = "authorId") Long authorId,
      @Valid @RequestBody AuthorEditRequest request,
      @RequestPart(name = "profileImg", required = false) MultipartFile profileImg) {
    AuthorEditResult result =
        authorService.updateAuthorProfile(
            loginAuthorId,
            authorId,
            request.to(),
            CustomFile.of(loginAuthorId, FileType.PROFILES, profileImg));
    AuthorEditResponse response = AuthorEditResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가 프로필 단건 조회
   *
   * @param authorId : 작가 아이디
   * @return authorId, profileImgUrl, nickname, introduction, subscribeCount, bookCount,
   *     restockCount
   */
  @GetMapping(path = "/{authorId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorDetailsResponse> getAuthorDetails(
      @PathVariable(name = "authorId") Long authorId) {
    AuthorDetailsResult result = authorService.findAuthorDetails(authorId);
    AuthorDetailsResponse response = AuthorDetailsResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가 프로필 다건 조회
   *
   * @return authorId, profileImgUrl, nickname, introduction, subscribeCount, bookCount,
   *     restockCount
   */
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorDetailsResponses> getAllAuthorDetails() {
    List<AuthorDetailsResult> results = authorService.findAllAuthorDetails();
    AuthorDetailsResponses responses = AuthorDetailsResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
