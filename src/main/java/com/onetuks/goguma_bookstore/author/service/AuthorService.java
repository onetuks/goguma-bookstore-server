package com.onetuks.goguma_bookstore.author.service;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorEditParam;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;
import com.onetuks.goguma_bookstore.global.service.FileURIProviderService;
import com.onetuks.goguma_bookstore.global.service.S3Service;
import com.onetuks.goguma_bookstore.global.service.vo.FileType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthorService {

  private final AuthorJpaRepository authorJpaRepository;

  private final S3Service s3Service;
  private final FileURIProviderService fileURIProviderService;

  public AuthorService(
      AuthorJpaRepository authorJpaRepository,
      S3Service s3Service,
      FileURIProviderService fileURIProviderService) {
    this.authorJpaRepository = authorJpaRepository;
    this.s3Service = s3Service;
    this.fileURIProviderService = fileURIProviderService;
  }

  @Transactional
  public AuthorEditResult updateAuthorProfile(
      long loginAuthorId,
      long authorId,
      AuthorEditParam authorEditParam,
      MultipartFile profileImg) {
    if (loginAuthorId != authorId) {
      throw new IllegalArgumentException("작가 정보를 수정할 권한이 없습니다.");
    }

    String newProfileImgUri = fileURIProviderService.provideFileURI(FileType.PROFILES, authorId);

    Author author =
        getAuthorById(authorId)
            .updateProfileImgUri(newProfileImgUri)
            .updateNickname(authorEditParam.nickname())
            .updateIntroduction(authorEditParam.introduction());

    s3Service.putFile(newProfileImgUri, profileImg);

    return AuthorEditResult.from(author);
  }

  @Transactional(readOnly = true)
  public AuthorDetailsResult findAuthorDetails(long authorId) {
    return AuthorDetailsResult.from(getAuthorById(authorId));
  }

  private Author getAuthorById(long authorId) {
    return authorJpaRepository
        .findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("해당 작가를 찾을 수 없습니다."));
  }
}
