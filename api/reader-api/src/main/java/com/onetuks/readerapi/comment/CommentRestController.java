package com.onetuks.readerapi.comment;

import com.onetuks.coreauth.util.login.MemberId;
import com.onetuks.coredomain.comment.model.Comment;
import com.onetuks.readerapi.comment.dto.request.CommentRequest;
import com.onetuks.readerapi.comment.dto.response.CommentResponse;
import com.onetuks.readerdomain.comment.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  /** 멤버 별 서평 조회 */

  /** 도서 별 서평 조회 */

  /** 서평 수정 */

  /** 서평 제거 */
}
