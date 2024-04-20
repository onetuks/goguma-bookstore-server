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
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    RegistrationEditResult result = RegistrationEditResult.from(save);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () -> assertThat(result.approvalResult()).isEqualTo(save.getApprovalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(save.getApprovalMemo()),
        () -> assertThat(result.title()).isEqualTo(save.getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(save.getOneLiner()),
        () -> assertThat(result.summary()).isEqualTo(save.getSummary()),
        () ->
            assertThat(result.categories())
                .containsExactlyInAnyOrderElementsOf(save.getCategories()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.isbn()).isEqualTo(save.getIsbn()),
        () -> assertThat(result.pageSizeInfo()).isEqualTo(save.getPageSizeInfo()),
        () -> assertThat(result.coverType()).isEqualTo(save.getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(save.getPageCount()),
        () -> assertThat(result.price()).isEqualTo(save.getPrice()),
        () -> assertThat(result.stockCount()).isEqualTo(save.getStockCount()),
        () -> assertThat(result.promotion()).isEqualTo(save.getPromotion()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(save.getCoverImgUrl()),
        () ->
            assertThat(result.detailImgUrls())
                .containsExactlyInAnyOrderElementsOf(save.getDetailImgUrls()),
        () ->
            assertThat(result.previewUrls())
                .containsExactlyInAnyOrderElementsOf(save.getPreviewUrls()),
        () -> assertThat(result.sampleUrl()).isEqualTo(save.getSampleUrl()));
  }
}
