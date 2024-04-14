package com.onetuks.goguma_bookstore.registration.service.dto.result;

import static org.assertj.core.api.Assertions.*;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegistrationCreateResultTest extends IntegrationTest {

  @Autowired private RegistrationJpaRepository registrationJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  private Author author;

  @BeforeEach
  void setUp() {
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    author = authorJpaRepository.save(AuthorFixture.create(member));
  }

  @Test
  @DisplayName("신간등록 엔티티에서 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    RegistrationCreateResult result = RegistrationCreateResult.from(save);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(save.getApprovalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(save.getApprovalMemo()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(save.getCoverImgUrl()),
        () -> assertThat(result.title()).isEqualTo(save.getTitle()),
        () -> assertThat(result.summary()).isEqualTo(save.getSummary()),
        () -> assertThat(result.price()).isEqualTo(save.getPrice()),
        () -> assertThat(result.stockCount()).isEqualTo(save.getStockCount()),
        () -> assertThat(result.isbn()).isEqualTo(save.getIsbn()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.promotion()).isEqualTo(save.getPromotion()),
        () -> assertThat(result.sampleUrl()).isEqualTo(save.getSampleUrl()));
  }
}
