package com.onetuks.modulereader.favorite.service;

import com.onetuks.modulecommon.error.ErrorCode;
import com.onetuks.modulecommon.exception.ApiAccessDeniedException;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.favorite.model.Favorite;
import com.onetuks.modulepersistence.favorite.repository.FavoriteJpaRepository;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulereader.favorite.service.dto.result.FavoriteGetResult;
import com.onetuks.modulereader.favorite.service.dto.result.FavoritePostResult;
import com.onetuks.modulereader.favorite.service.dto.result.FavoriteWhetherGetResult;
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
              if (favorite.getMember().getMemberId() != memberId) {
                throw new ApiAccessDeniedException(ErrorCode.UNAUTHORITY_ACCESS_DENIED);
              }

              favorite.getBook().getBookStatics().decreaseFavoriteCount();
              favoriteJpaRepository.delete(favorite);
            });
  }

  @Transactional(readOnly = true)
  public FavoriteWhetherGetResult readFavoriteExistence(long memberId, long bookId) {
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
