package com.onetuks.modulescm.registration.service.dto.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.author.repository.AuthorJpaRepository;
import com.onetuks.modulepersistence.fixture.AuthorFixture;
import com.onetuks.modulepersistence.fixture.MemberFixture;
import com.onetuks.modulepersistence.fixture.RegistrationFixture;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.member.repository.MemberJpaRepository;
import com.onetuks.modulepersistence.registration.model.Registration;
import com.onetuks.modulepersistence.registration.repository.RegistrationJpaRepository;
import com.onetuks.modulescm.ScmIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegistrationResultTest extends ScmIntegrationTest {

  @Autowired private RegistrationJpaRepository registrationJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private AuthorJpaRepository authorJpaRepository;

  @Test
  @DisplayName("신간등록 엔티티에서 신간등록 조회 결과 객체로 변환한다.")
  void fromTest() {
    // Given
    Member member = memberJpaRepository.save(MemberFixture.create(RoleType.AUTHOR));
    Author author = authorJpaRepository.save(AuthorFixture.create(member));
    Registration save = registrationJpaRepository.save(RegistrationFixture.create(author));

    // When
    RegistrationResult result = RegistrationResult.from(save);

    // Then
    assertAll(
        () -> assertThat(result.registrationId()).isEqualTo(save.getRegistrationId()),
        () ->
            assertThat(result.approvalResult())
                .isEqualTo(save.getApprovalInfo().getApprovalResult()),
        () -> assertThat(result.approvalMemo()).isEqualTo(save.getApprovalInfo().getApprovalMemo()),
        () -> assertThat(result.title()).isEqualTo(save.getBookConceptualInfo().getTitle()),
        () -> assertThat(result.oneLiner()).isEqualTo(save.getBookConceptualInfo().getOneLiner()),
        () -> assertThat(result.summary()).isEqualTo(save.getBookConceptualInfo().getSummary()),
        () ->
            assertThat(result.categories()).isEqualTo(save.getBookConceptualInfo().getCategories()),
        () -> assertThat(result.isbn()).isEqualTo(save.getBookConceptualInfo().getIsbn()),
        () -> assertThat(result.height()).isEqualTo(save.getBookPhysicalInfo().getHeight()),
        () -> assertThat(result.width()).isEqualTo(save.getBookPhysicalInfo().getWidth()),
        () -> assertThat(result.coverType()).isEqualTo(save.getBookPhysicalInfo().getCoverType()),
        () -> assertThat(result.pageCount()).isEqualTo(save.getBookPhysicalInfo().getPageCount()),
        () ->
            assertThat(result.regularPrice()).isEqualTo(save.getBookPriceInfo().getRegularPrice()),
        () ->
            assertThat(result.purchasePrice())
                .isEqualTo(save.getBookPriceInfo().getPurchasePrice()),
        () -> assertThat(result.promotion()).isEqualTo(save.getBookPriceInfo().getPromotion()),
        () -> assertThat(result.publisher()).isEqualTo(save.getPublisher()),
        () -> assertThat(result.stockCount()).isEqualTo(save.getStockCount()),
        () -> assertThat(result.coverImgUrl()).isEqualTo(save.getCoverImgUrl()),
        () -> assertThat(result.detailImgUrls()).isEqualTo(save.getDetailImgUrls()),
        () -> assertThat(result.previewUrls()).isEqualTo(save.getPreviewUrls()),
        () -> assertThat(result.sampleUrl()).isEqualTo(save.getSampleUrl()));
  }
}
