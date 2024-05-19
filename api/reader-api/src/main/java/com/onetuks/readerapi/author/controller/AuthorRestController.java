package com.onetuks.readerapi.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.readerapi.author.dto.response.AuthorGetResponse;
import com.onetuks.readerapi.author.dto.response.AuthorGetResponse.AuthorGetReponses;
import com.onetuks.readerdomain.author.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
   * 작가 프로필 상세 조회
   *
   * @param authorId : 작가 ID
   * @return 200 OK
   */
  @GetMapping(path = "/{authorId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorGetResponse> getAuthorDetails(
      @PathVariable(name = "authorId") Long authorId) {
    Author result = authorService.readAuthorDetails(authorId);
    AuthorGetResponse response = AuthorGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 작가 프로필 상세 조회
   *
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorGetReponses> getAllAuthorDetails(
      @PageableDefault(sort = "authorId", direction = Direction.DESC) Pageable pageable) {
    Page<Author> results = authorService.readAllAuthorDetails(pageable);
    AuthorGetReponses responses = AuthorGetReponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
