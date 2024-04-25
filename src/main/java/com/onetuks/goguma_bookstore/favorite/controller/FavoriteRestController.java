package com.onetuks.goguma_bookstore.favorite.controller;

import com.onetuks.goguma_bookstore.auth.util.login.LoginId;
import com.onetuks.goguma_bookstore.favorite.controller.dto.response.FavoritePostResponse;
import com.onetuks.goguma_bookstore.favorite.service.FavoriteService;
import com.onetuks.goguma_bookstore.favorite.service.dto.result.FavoritePostResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/favorites")
public class FavoriteRestController {

  private final FavoriteService favoriteService;

  public FavoriteRestController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
  }

  /**
   * 즐겨찾기 추가
   *
   * @param memberId : 로그인한 회원의 ID
   * @param bookId : 즐겨찾기에 추가할 도서의 ID
   * @return FavoritePostResponse
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FavoritePostResponse> addFavorite(
      @LoginId Long memberId, @RequestParam(name = "bookId") Long bookId) {
    FavoritePostResult result = favoriteService.createFavorite(memberId, bookId);
    FavoritePostResponse response = FavoritePostResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 즐겨찾기 삭제
   *
   * @param memberId : 로그인 아이디
   * @param favoriteId : 즐겨찾기 아이디
   * @return Void
   */
  @DeleteMapping(path = "/{favoriteId}")
  public ResponseEntity<Void> removeFavorite(
      @LoginId Long memberId, @PathVariable(name = "favoriteId") Long favoriteId) {
    favoriteService.deleteFavorite(memberId, favoriteId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
