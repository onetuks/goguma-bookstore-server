package com.onetuks.modulereader.author.service;

import com.onetuks.modulecommon.file.FileWrapper;
import com.onetuks.modulecommon.service.S3Repository;
import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulereader.author.service.dto.param.AuthorEditParam;
import com.onetuks.modulereader.author.service.dto.result.AuthorDetailsResult;
import com.onetuks.modulereader.author.service.dto.result.AuthorEditResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

  private final AuthorJpaRepository authorJpaRepository;
  private final S3Repository s3Repository;

  public AuthorService(AuthorJpaRepository authorJpaRepository, S3Repository s3Repository) {
    this.authorJpaRepository = authorJpaRepository;
    this.s3Repository = s3Repository;
  }

  @Transactional
  public AuthorEditResult updateAuthorProfile(
      long loginAuthorId,
      long authorId,
      AuthorEditParam authorEditParam,
      FileWrapper profileImgFile) {
    if (loginAuthorId != authorId) {
      throw new IllegalArgumentException("작가 정보를 수정할 권한이 없습니다.");
    }

    s3Repository.putFile(profileImgFile);

    return AuthorEditResult.from(
        getAuthorById(authorId)
            .changeProfileImgPath(profileImgFile.getUri())
            .changeAuthorProfile(
                authorEditParam.nickname(),
                authorEditParam.introduction(),
                authorEditParam.instagramUrl()));
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
  public AuthorEntity getAuthorById(long authorId) {
    return authorJpaRepository
        .findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("해당 작가를 찾을 수 없습니다."));
  }
}
