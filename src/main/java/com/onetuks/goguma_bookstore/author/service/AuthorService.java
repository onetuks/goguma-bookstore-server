package com.onetuks.goguma_bookstore.author.service;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorEditParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

  private final AuthorJpaRepository authorJpaRepository;
  private final S3Service s3Service;

  public AuthorService(AuthorJpaRepository authorJpaRepository, S3Service s3Service) {
    this.authorJpaRepository = authorJpaRepository;
    this.s3Service = s3Service;
  }

  @Transactional
  public AuthorEditResult updateAuthorProfile(
      long loginAuthorId, long authorId, AuthorEditParam authorEditParam, CustomFile customFile) {
    if (loginAuthorId != authorId) {
      throw new IllegalArgumentException("작가 정보를 수정할 권한이 없습니다.");
    }

    s3Service.putFile(customFile);

    return AuthorEditResult.from(
        getAuthorById(authorId)
            .updateProfileImgFile(customFile.toProfileImgFile())
            .updateNickname(authorEditParam.nickname())
            .updateIntroduction(authorEditParam.introduction()));
  }

  @Transactional(readOnly = true)
  public AuthorDetailsResult findAuthorDetails(long authorId) {
    return AuthorDetailsResult.from(getAuthorById(authorId));
  }

  @Transactional(readOnly = true)
  public List<AuthorDetailsResult> findAllAuthorDetails() {
    return authorJpaRepository.findAuthorsByEnrollmentInfoEnrollmentPassedTrue().stream()
        .map(AuthorDetailsResult::from)
        .toList();
  }

  private Author getAuthorById(long authorId) {
    return authorJpaRepository
        .findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("해당 작가를 찾을 수 없습니다."));
  }
}
