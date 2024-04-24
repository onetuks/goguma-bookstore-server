package com.onetuks.goguma_bookstore.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.RegistrationFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
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
        () -> assertThat(result.getCoverImgFile()).isEqualTo(registration.getCoverImgFile()),
        () -> assertThat(result.getDetailImgFiles()).isEqualTo(registration.getDetailImgFiles()),
        () -> assertThat(result.getPreviewFiles()).isEqualTo(registration.getPreviewFiles()));
  }
}
