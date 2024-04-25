package com.onetuks.goguma_bookstore.favorite.service;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.favorite.model.Favorite;
import com.onetuks.goguma_bookstore.favorite.repository.FavoriteJpaRepository;
import com.onetuks.goguma_bookstore.favorite.service.dto.result.FavoriteGetResult;
import com.onetuks.goguma_bookstore.favorite.service.dto.result.FavoritePostResult;
import com.onetuks.goguma_bookstore.favorite.service.dto.result.FavoriteWhetherGetResult;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

  private final FavoriteJpaRepository favoriteJpaRepository;
  private final MemberJpaRepository memberJpaRepository;
  private final BookJpaRepository bookJpaRepository;

  public FavoriteService(
      FavoriteJpaRepository favoriteJpaRepository,
      MemberJpaRepository memberJpaRepository,
      BookJpaRepository bookJpaRepository) {
    this.favoriteJpaRepository = favoriteJpaRepository;
    this.memberJpaRepository = memberJpaRepository;
    this.bookJpaRepository = bookJpaRepository;
  }

  @Transactional(readOnly = true)
  public FavoritePostResult createFavorite(long memberId, long bookId) {
    Member member = memberJpaRepository.findById(memberId).orElseThrow();
    Book book =
        bookJpaRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 도서입니다."));

    book.getBookStatics().increaseFavoriteCount();

    return FavoritePostResult.from(
        favoriteJpaRepository.save(Favorite.builder().member(member).book(book).build()));
  }

  @Transactional
  public void deleteFavorite(long memberId, long favoriteId) {
    favoriteJpaRepository
        .findById(favoriteId)
        .ifPresent(
            favorite -> {
              if (favorite.getMember().getMemberId() == memberId) {
                favorite.getBook().getBookStatics().decreaseFavoriteCount();
                favoriteJpaRepository.delete(favorite);
              }
            });
  }

  @Transactional(readOnly = true)
  public FavoriteWhetherGetResult readFavoriteExistance(long memberId, long bookId) {
    return FavoriteWhetherGetResult.from(
        favoriteJpaRepository.existsByMemberMemberIdAndBookBookId(memberId, bookId));
  }

  @Transactional(readOnly = true)
  public Page<FavoriteGetResult> readFavoritesOfMember(long memberId, Pageable pageable) {
    return favoriteJpaRepository
        .findAllByMemberMemberId(memberId, pageable)
        .map(favorite -> FavoriteGetResult.from(favorite, favorite.getBook()));
  }
}
