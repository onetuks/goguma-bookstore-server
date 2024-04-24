package com.onetuks.goguma_bookstore.author.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorDetailsResponse;
import com.onetuks.goguma_bookstore.author.controller.dto.response.AuthorDetailsResponse.AuthorDetailsResponses;
import com.onetuks.goguma_bookstore.author.service.AuthorService;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
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
   * 작가 프로필 단건 조회
   *
   * @param authorId : 작가 아이디
   * @return authorId, profileImgUrl, nickname, introduction, subscribeCount, bookCount,
   *     restockCount
   */
  @GetMapping(path = "/{authorId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorDetailsResponse> getAuthorDetails(
      @PathVariable(name = "authorId") Long authorId) {
    AuthorDetailsResult result = authorService.readAuthorDetails(authorId);
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
  public ResponseEntity<AuthorDetailsResponses> getAllAuthorDetails(
      @PageableDefault(sort = "authorId", direction = Direction.DESC) Pageable pageable) {
    Page<AuthorDetailsResult> results = authorService.readAllAuthorDetails(pageable);
    AuthorDetailsResponses responses = AuthorDetailsResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
