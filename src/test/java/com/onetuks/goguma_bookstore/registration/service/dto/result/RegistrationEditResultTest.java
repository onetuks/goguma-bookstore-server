package com.onetuks.goguma_bookstore.registration.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.goguma_bookstore.IntegrationTest;
import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.repository.AuthorJpaRepository;
import com.onetuks.goguma_bookstore.fixture.AuthorFixture;
import com.onetuks.goguma_bookstore.fixture.MemberFixture;
import com.onetuks.goguma_bookstore.fixture.RegistrationFixture;
import com.onetuks.goguma_bookstore.global.vo.auth.RoleType;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.member.repository.MemberJpaRepository;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import com.onetuks.goguma_bookstore.registration.repository.RegistrationJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegistrationEditResultTest extends IntegrationTest {

  @Autowired private RegistrationJpaRepository registrationJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  @Test
  @DisplayName("신간등록 엔티티에서 신간등록 수정 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author author = authorJpaRepository.save(AuthorFixture.create(member));
    Registration registration = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    RegistrationEditResult result = RegistrationEditResult.from(registration);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(registration.getRegistrationId()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(registration.getCoverImgUrl()),
        () -> assertThat(result.title()).isEqualTo(registration.getTitle()),
        () -> assertThat(result.summary()).isEqualTo(registration.getSummary()),
        () -> assertThat(result.price()).isEqualTo(registration.getPrice()),
        () -> assertThat(result.stockCount()).isEqualTo(registration.getStockCount()),
        () -> assertThat(result.isbn()).isEqualTo(registration.getIsbn()),
        () -> assertThat(result.publisher()).isEqualTo(registration.getPublisher()),
        () -> assertThat(result.promotion()).isEqualTo(registration.getPromotion()),
        () -> assertThat(result.sampleUrl()).isEqualTo(registration.getSampleUrl()));
  }
}
