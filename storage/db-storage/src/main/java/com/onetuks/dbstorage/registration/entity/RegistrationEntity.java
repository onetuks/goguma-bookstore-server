package com.onetuks.dbstorage.registration.entity;

import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.book.entity.embedded.BookConceptualEmbedded;
import com.onetuks.dbstorage.book.entity.embedded.BookPhysicalInfoEmbedded;
import com.onetuks.dbstorage.book.entity.embedded.BookPriceInfoEmbedded;
import com.onetuks.dbstorage.book.vo.Category;
import com.onetuks.dbstorage.order.vo.CoverImgFilePath;
import com.onetuks.dbstorage.order.vo.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.dbstorage.order.vo.PreviewFilePath.PreviewFilePaths;
import com.onetuks.dbstorage.order.vo.SampleFilePath;
import com.onetuks.dbstorage.registration.entity.embedded.ApprovalInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "registrations")
public class RegistrationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "registration_id", nullable = false)
  private Long registrationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private AuthorEntity authorEntity;

  @Embedded private ApprovalInfo approvalInfo;

  @Embedded private BookConceptualEmbedded bookConceptualEmbedded;

  @Embedded private BookPhysicalInfoEmbedded bookPhysicalInfoEmbedded;

  @Embedded private BookPriceInfoEmbedded bookPriceInfoEmbedded;

  @Embedded private CoverImgFilePath coverImgFilePath;

  @Embedded private DetailImgFilePaths detailImgFilePaths;

  @Embedded private PreviewFilePaths previewFilePaths;

  @Embedded private SampleFilePath sampleFilePath;

  @Builder
  public RegistrationEntity(
      AuthorEntity authorEntity,
      ApprovalInfo approvalInfo,
      BookConceptualEmbedded bookConceptualEmbedded,
      BookPhysicalInfoEmbedded bookPhysicalInfoEmbedded,
      BookPriceInfoEmbedded bookPriceInfoEmbedded,
      String coverImgFilePath,
      List<String> detailImgFilePaths,
      List<String> previewFilePaths,
      String sampleFilePath) {
    this.authorEntity = authorEntity;
    this.approvalInfo = Objects.requireNonNullElse(approvalInfo, ApprovalInfo.init());
    this.bookConceptualEmbedded = bookConceptualEmbedded;
    this.bookPhysicalInfoEmbedded = bookPhysicalInfoEmbedded;
    this.bookPriceInfoEmbedded = bookPriceInfoEmbedded;
    this.coverImgFilePath = CoverImgFilePath.of(coverImgFilePath);
    this.detailImgFilePaths = DetailImgFilePaths.of(detailImgFilePaths);
    this.previewFilePaths = PreviewFilePaths.of(previewFilePaths);
    this.sampleFilePath = SampleFilePath.of(sampleFilePath);
  }

  public boolean getApprovalResult() {
    return this.approvalInfo.getApprovalResult();
  }

  public String getApprovalMemo() {
    return this.approvalInfo.getApprovalMemo();
  }

  public String getTitle() {
    return this.bookConceptualEmbedded.getTitle();
  }

  public String getOneLiner() {
    return this.bookConceptualEmbedded.getOneLiner();
  }

  public String getSummary() {
    return this.bookConceptualEmbedded.getSummary();
  }

  public List<Category> getCategories() {
    return this.bookConceptualEmbedded.getCategories();
  }

  public String getPublisher() {
    return this.bookConceptualEmbedded.getPublisher();
  }

  public String getIsbn() {
    return this.bookConceptualEmbedded.getIsbn();
  }

  public int getHeight() {
    return this.bookPhysicalInfoEmbedded.getHeight();
  }

  public int getWidth() {
    return this.bookPhysicalInfoEmbedded.getWidth();
  }

  public String getCoverType() {
    return this.bookPhysicalInfoEmbedded.getCoverType();
  }

  public long getPageCount() {
    return this.bookPhysicalInfoEmbedded.getPageCount();
  }

  public long getRegularPrice() {
    return this.bookPriceInfoEmbedded.getRegularPrice();
  }

  public long getPurchasePrice() {
    return this.bookPriceInfoEmbedded.getPurchasePrice();
  }

  public boolean isPromotion() {
    return this.bookPriceInfoEmbedded.getPromotion();
  }

  public long getStockCount() {
    return this.bookPriceInfoEmbedded.getStockCount();
  }

  public String getCoverImgUri() {
    return this.coverImgFilePath.getUri();
  }

  public String getCoverImgUrl() {
    return this.coverImgFilePath.getUrl();
  }

  public List<String> getDetailImgUris() {
    return detailImgFilePaths.getUris();
  }

  public List<String> getDetailImgUrls() {
    return detailImgFilePaths.getUrls();
  }

  public List<String> getPreviewUris() {
    return previewFilePaths.getUris();
  }

  public List<String> getPreviewUrls() {
    return previewFilePaths.getUrls();
  }

  public String getSampleUrl() {
    return this.sampleFilePath.getUrl();
  }

  public RegistrationEntity changeApprovalInfo(boolean approvalResult, String approvalMemo) {
    this.approvalInfo =
        ApprovalInfo.builder().approvalResult(approvalResult).approvalMemo(approvalMemo).build();
    return this;
  }

  public RegistrationEntity changeRegistration(
      BookConceptualEmbedded bookConceptualEmbedded,
      BookPriceInfoEmbedded bookPriceInfoEmbedded,
      String coverImgFilePath,
      List<String> detailImgFiles,
      List<String> previewFiles,
      String sampleFile) {
    this.approvalInfo = ApprovalInfo.init();
    this.bookConceptualEmbedded = bookConceptualEmbedded;
    this.bookPriceInfoEmbedded = bookPriceInfoEmbedded;
    this.coverImgFilePath = CoverImgFilePath.of(coverImgFilePath);
    this.detailImgFilePaths = DetailImgFilePaths.of(detailImgFiles);
    this.previewFilePaths = PreviewFilePaths.of(previewFiles);
    this.sampleFilePath = SampleFilePath.of(sampleFile);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistrationEntity that = (RegistrationEntity) o;
    return Objects.equals(registrationId, that.registrationId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(registrationId);
  }
}
