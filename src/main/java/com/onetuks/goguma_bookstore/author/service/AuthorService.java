package com.onetuks.goguma_bookstore.author.service;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

  private final AuthorJpaRepository authorJpaRepository;

  public AuthorService(AuthorJpaRepository authorJpaRepository) {
    this.authorJpaRepository = authorJpaRepository;
  }

  @Transactional(readOnly = true)
  public AuthorDetailsResult readAuthorDetails(long authorId) {
    return AuthorDetailsResult.from(getAuthorById(authorId));
  }

  @Transactional(readOnly = true)
  public Page<AuthorDetailsResult> readAllAuthorDetails(Pageable pageable) {
    return authorJpaRepository
        .findAuthorsByEnrollmentInfoEnrollmentPassedTrue(pageable)
        .map(AuthorDetailsResult::from);
  }

  @Transactional(readOnly = true)
  public Author getAuthorById(long authorId) {
    return authorJpaRepository
        .findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("해당 작가를 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public Long getAuthorIdByMemberId(long loginId) {
    return authorJpaRepository
        .findByMemberMemberId(loginId)
        .orElseThrow(() -> new EntityNotFoundException("해당 작가를 찾을 수 없습니다."))
        .getAuthorId();
  }
}
