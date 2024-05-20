package com.onetuks.readerapi.comment.controller;

import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.coredomain.comment.model.Comment;
import com.onetuks.readerapi.comment.dto.request.CommentRequest;
import com.onetuks.readerapi.comment.dto.response.CommentResponse;
import com.onetuks.readerapi.comment.dto.response.CommentResponse.CommentResponses;
import com.onetuks.readerdomain.comment.service.CommentService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/comments")
public class CommentRestController {

  private final CommentService commentService;

  public CommentRestController(CommentService commentService) {
    this.commentService = commentService;
  }

  /**
   * 서평 작성
   *
   * @param memberId : 로그인한 멤버 ID
   * @param bookId : 도서 ID
   * @param request : 서평 요청
   * @return 200 OK
   */
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommentResponse> postNewComment(
      @MemberId Long memberId,
      @RequestParam(name = "bookId") Long bookId,
      @RequestBody @Valid CommentRequest request) {
    Comment result =
        commentService.createComment(memberId, bookId, request.title(), request.content());
    CommentResponse response = CommentResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 서평 조회
   *
   * @param commentId : 서평 ID
   * @return 200 OK
   */
  @GetMapping(path = "/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommentResponse> getComment(
      @PathVariable(name = "commentId") Long commentId) {
    Comment result = commentService.readComment(commentId);
    CommentResponse response = CommentResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 멤버 별 서평 조회
   *
   * @param memberId : 멤버 ID
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(path = "/members/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommentResponses> getAllCommentsOfMember(
      @MemberId Long memberId,
      @PageableDefault(sort = "commentId", direction = Direction.DESC) Pageable pageable) {
    Page<Comment> results = commentService.readAllCommentsOfMember(memberId, pageable);
    CommentResponses responses = CommentResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 도서 별 서평 조회
   *
   * @param bookId : 도서 ID
   * @param pageable : 페이지 정보
   * @return 200 OK
   */
  @GetMapping(path = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommentResponses> getAllCommentsOfBook(
      @PathVariable(name = "bookId") Long bookId,
      @PageableDefault(sort = "commentId", direction = Direction.DESC) Pageable pageable) {
    Page<Comment> results = commentService.readAllCommentsOfBook(bookId, pageable);
    CommentResponses responses = CommentResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 서평 수정
   *
   * @param memberId : 로그인한 멤버 ID
   * @param commentId : 서평 ID
   * @param request : 서평 요청
   * @return 200 OK
   */
  @PatchMapping(
      path = "/{commentId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommentResponse> patchComment(
      @MemberId Long memberId,
      @PathVariable(name = "commentId") Long commentId,
      @RequestBody @Valid CommentRequest request) {
    Comment result =
        commentService.updateComment(memberId, commentId, request.title(), request.content());
    CommentResponse response = CommentResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 서평 삭제
   *
   * @param memberId : 로그인한 멤버 ID
   * @param commentId : 서평 ID
   * @return 204 NO_CONTENT
   */
  @DeleteMapping(path = "/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @MemberId Long memberId, @PathVariable(name = "commentId") Long commentId) {
    commentService.deleteComment(memberId, commentId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
