package com.onetuks.readerdomain.favorite.service;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.coredomain.favorite.repository.FavoriteRepository;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.member.repository.MemberRepository;
import com.onetuks.coreobj.exception.ApiAccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;

  public FavoriteService(
      FavoriteRepository favoriteRepository,
      MemberRepository memberRepository,
      BookRepository bookRepository) {
    this.favoriteRepository = favoriteRepository;
    this.memberRepository = memberRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional(readOnly = true)
  public Favorite createFavorite(long memberId, long bookId) {
    Member member = memberRepository.read(memberId);
    Book book = bookRepository.read(bookId);

    return favoriteRepository.create(new Favorite(null, member, book));
  }

  @Transactional(readOnly = true)
  public boolean readFavoriteExistence(long memberId, long bookId) {
    return favoriteRepository.readExistence(memberId, bookId);
  }

  @Transactional(readOnly = true)
  public Page<Favorite> readAllFavorites(long memberId, Pageable pageable) {
    return favoriteRepository.readAll(memberId, pageable);
  }

  @Transactional
  public void deleteFavorite(long memberId, long favoriteId) {
    Long targetMemberId = favoriteRepository.read(favoriteId).member().memberId();

    if (targetMemberId != memberId) {
      throw new ApiAccessDeniedException("해당 즐겨찾기에 대한 권한이 없는 멤버입니다.");
    }

    favoriteRepository.delete(favoriteId);
  }
}
