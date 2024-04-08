package com.onetuks.goguma_bookstore.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.auth.util.LoginId;
import com.onetuks.goguma_bookstore.author.controller.dto.request.AuthorCreateRequest;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorCreateResponse;
import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/authors")
public class AuthorRestController {

  private final AuthorService authorService;

  public AuthorRestController(AuthorService authorService) {
    this.authorService = authorService;
  }

  /**
   * 작가 등록 신청
   *
   * @param loginId
   * @param request
   * @return
   */
  @PostMapping(
      path = "/enrollment",
      produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorCreateResponse> createEnrollRequest(
      @LoginId Long loginId, @Valid @RequestBody AuthorCreateRequest request) {
    AuthorCreateResult result = authorService.createAuthorDebut(loginId, request.to());
    AuthorCreateResponse response = AuthorCreateResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
