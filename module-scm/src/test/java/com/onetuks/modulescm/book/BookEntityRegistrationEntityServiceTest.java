package com.onetuks.modulescm.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.entity.BookEntity;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.fixture.RegistrationFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.registration.entity.RegistrationEntity;
import com.onetuks.modulepersistence.registration.repository.RegistrationJpaRepository;
import com.onetuks.modulescm.ScmIntegrationTest;
import com.onetuks.modulescm.book.service.BookRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BookEntityRegistrationEntityServiceTest extends ScmIntegrationTest {

  @Autowired private BookRegistrationService bookRegistrationService;
  @Autowired private BookJpaRepository bookJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private RegistrationJpaRepository registrationJpaRepository;

  @Test
  void createBookTest() {
    // Given
    RegistrationEntity registrationEntity =
        registrationJpaRepository.save(
            RegistrationFixture.create(
                authorJpaRepository.save(
                    AuthorFixture.create(
                        memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))))));

    // When
    long bookId = bookRegistrationService.createBook(registrationEntity);

    // Then
    BookEntity result =
        bookJpaRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("책 등록에 실패했습니다."));

    assertThat(bookId).isPositive();
    assertAll(
        () -> assertThat(result.getBookId()).isEqualTo(bookId),
        () ->
            assertThat(result.getBookConceptualInfo())
                .isEqualTo(registrationEntity.getBookConceptualInfo()),
        () ->
            assertThat(result.getBookPhysicalInfo()).isEqualTo(registrationEntity.getBookPhysicalInfo()),
        () -> assertThat(result.getBookPriceInfo()).isEqualTo(registrationEntity.getBookPriceInfo()),
        () -> assertThat(result.getPublisher()).isEqualTo(registrationEntity.getPublisher()),
        () -> assertThat(result.getStockCount()).isEqualTo(registrationEntity.getStockCount()),
        () -> assertThat(result.getCoverImgUrl()).isEqualTo(registrationEntity.getCoverImgUrl()),
        () -> assertThat(result.getDetailImgUrls()).isEqualTo(registrationEntity.getDetailImgUrls()),
        () -> assertThat(result.getPreviewUrls()).isEqualTo(registrationEntity.getPreviewUrls()));
  }
}
