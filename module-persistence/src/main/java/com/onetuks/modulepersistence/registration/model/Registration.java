package com.onetuks.modulepersistence.registration.model;


import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.book.model.embedded.BookConceptualInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPhysicalInfo;
import com.onetuks.modulepersistence.book.model.embedded.BookPriceInfo;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.global.vo.file.CoverImgFilePath;
import com.onetuks.modulepersistence.global.vo.file.DetailImgFilePath;
import com.onetuks.modulepersistence.global.vo.file.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.modulepersistence.global.vo.file.PreviewFilePath;
import com.onetuks.modulepersistence.global.vo.file.PreviewFilePath.PreviewFilePaths;
import com.onetuks.modulepersistence.global.vo.file.SampleFilePath;
import com.onetuks.modulepersistence.registration.model.embedded.ApprovalInfo;
import io.hypersistence.utils.hibernate.type.json.JsonType;
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
import org.hibernate.annotations.Type;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "registrations")
public class Registration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "registration_id", nullable = false)
  private Long registrationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @Embedded
  private ApprovalInfo approvalInfo;

  @Embedded private BookConceptualInfo bookConceptualInfo;

  @Embedded private BookPhysicalInfo bookPhysicalInfo;

  @Embedded private BookPriceInfo bookPriceInfo;

  @Embedded private CoverImgFilePath coverImgFilePath;

  @Embedded private DetailImgFilePaths detailImgFilePaths;

  @Embedded private PreviewFilePaths previewFilePaths;

  @Embedded private SampleFilePath sampleFilePath;

  @Builder
  public Registration(
      Author author,
      ApprovalInfo approvalInfo,
      BookConceptualInfo bookConceptualInfo,
      BookPhysicalInfo bookPhysicalInfo,
      BookPriceInfo bookPriceInfo,
      String coverImgFilePath,
      List<String> detailImgFilePaths,
      List<String> previewFilePaths,
      String sampleFilePath) {
    this.author = author;
    this.approvalInfo = Objects.requireNonNullElse(approvalInfo, ApprovalInfo.init());
    this.bookConceptualInfo = bookConceptualInfo;
    this.bookPhysicalInfo = bookPhysicalInfo;
    this.bookPriceInfo = bookPriceInfo;
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
    return this.bookConceptualInfo.getTitle();
  }

  public String getOneLiner() {
    return this.bookConceptualInfo.getOneLiner();
  }

  public String getSummary() {
    return this.bookConceptualInfo.getSummary();
  }

  public List<Category> getCategories() {
    return this.bookConceptualInfo.getCategories();
  }

  public String getPublisher() {
    return this.bookConceptualInfo.getPublisher();
  }

  public String getIsbn() {
    return this.bookConceptualInfo.getIsbn();
  }

  public int getHeight() {
    return this.bookPhysicalInfo.getHeight();
  }

  public int getWidth() {
    return this.bookPhysicalInfo.getWidth();
  }

  public String getCoverType() {
    return this.bookPhysicalInfo.getCoverType();
  }

  public long getPageCount() {
    return this.bookPhysicalInfo.getPageCount();
  }

  public long getRegularPrice() {
    return this.bookPriceInfo.getRegularPrice();
  }

  public long getPurchasePrice() {
    return this.bookPriceInfo.getPurchasePrice();
  }

  public boolean isPromotion() {
    return this.bookPriceInfo.getPromotion();
  }

  public long getStockCount() {
    return this.bookPriceInfo.getStockCount();
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

  public Registration changeApprovalInfo(boolean approvalResult, String approvalMemo) {
    this.approvalInfo =
        ApprovalInfo.builder().approvalResult(approvalResult).approvalMemo(approvalMemo).build();
    return this;
  }

  public Registration changeRegistration(
      BookConceptualInfo bookConceptualInfo,
      BookPriceInfo bookPriceInfo,
      String coverImgFilePath,
      List<String> detailImgFiles,
      List<String> previewFiles,
      String sampleFile) {
    this.approvalInfo = ApprovalInfo.init();
    this.bookConceptualInfo = bookConceptualInfo;
    this.bookPriceInfo = bookPriceInfo;
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
    Registration that = (Registration) o;
    return Objects.equals(registrationId, that.registrationId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(registrationId);
  }
}
