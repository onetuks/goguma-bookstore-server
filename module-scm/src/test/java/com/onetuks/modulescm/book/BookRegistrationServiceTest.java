package com.onetuks.modulescm.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.fixture.RegistrationFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.registration.model.Registration;
import com.onetuks.modulepersistence.registration.repository.RegistrationJpaRepository;
import com.onetuks.modulescm.IntegrationTest;
import com.onetuks.modulescm.book.service.BookRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BookRegistrationServiceTest extends IntegrationTest {

  @Autowired private BookRegistrationService bookRegistrationService;
  @Autowired private BookJpaRepository bookJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;
  @Autowired private RegistrationJpaRepository registrationJpaRepository;

  @Test
  void createBookTest() {
    // Given
    Registration registration =
        registrationJpaRepository.save(
            RegistrationFixture.create(
                authorJpaRepository.save(
                    AuthorFixture.create(
                        memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR))))));

    // When
    long bookId = bookRegistrationService.createBook(registration);

    // Then
    Book result =
        bookJpaRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("책 등록에 실패했습니다."));

    assertThat(bookId).isPositive();
    assertAll(
        () -> assertThat(result.getBookId()).isEqualTo(bookId),
        () ->
            assertThat(result.getBookConceptualInfo())
                .isEqualTo(registration.getBookConceptualInfo()),
        () ->
            assertThat(result.getBookPhysicalInfo()).isEqualTo(registration.getBookPhysicalInfo()),
        () -> assertThat(result.getBookPriceInfo()).isEqualTo(registration.getBookPriceInfo()),
        () -> assertThat(result.getPublisher()).isEqualTo(registration.getPublisher()),
        () -> assertThat(result.getStockCount()).isEqualTo(registration.getStockCount()),
        () -> assertThat(result.getCoverImgUrl()).isEqualTo(registration.getCoverImgUrl()),
        () -> assertThat(result.getDetailImgUrls()).isEqualTo(registration.getDetailImgUrls()),
        () -> assertThat(result.getPreviewUrls()).isEqualTo(registration.getPreviewUrls()));
  }
}
