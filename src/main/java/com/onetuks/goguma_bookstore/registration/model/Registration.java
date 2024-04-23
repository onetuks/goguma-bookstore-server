package com.onetuks.goguma_bookstore.registration.model;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.book.model.converter.CustomFileListToJsonConverter;
import com.onetuks.goguma_bookstore.book.model.vo.BookConceptualInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPhysicalInfo;
import com.onetuks.goguma_bookstore.book.model.vo.BookPriceInfo;
import com.onetuks.goguma_bookstore.global.vo.file.CoverImgFile;
import com.onetuks.goguma_bookstore.global.vo.file.DetailImgFile;
import com.onetuks.goguma_bookstore.global.vo.file.PreviewFile;
import com.onetuks.goguma_bookstore.global.vo.file.SampleFile;
import com.onetuks.goguma_bookstore.registration.model.vo.ApprovalInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
public class Registration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "registration_id", nullable = false)
  private Long registrationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @Embedded private ApprovalInfo approvalInfo;

  @Embedded private BookConceptualInfo bookConceptualInfo;

  @Embedded private BookPhysicalInfo bookPhysicalInfo;

  @Embedded private BookPriceInfo bookPriceInfo;

  @Column(name = "publisher", nullable = false)
  private String publisher;

  @Column(name = "stock_count", nullable = false)
  private Long stockCount;

  @Embedded private CoverImgFile coverImgFile;

  @Convert(converter = CustomFileListToJsonConverter.class)
  @Column(name = "detail_img_uris", nullable = false)
  private List<DetailImgFile> detailImgFiles;

  @Convert(converter = CustomFileListToJsonConverter.class)
  @Column(name = "preview_uris", nullable = false)
  private List<PreviewFile> previewFiles;

  @Embedded private SampleFile sampleFile;

  @Builder
  public Registration(
      Author author,
      ApprovalInfo approvalInfo,
      BookConceptualInfo bookConceptualInfo,
      BookPhysicalInfo bookPhysicalInfo,
      BookPriceInfo bookPriceInfo,
      String publisher,
      Long stockCount,
      CoverImgFile coverImgFile,
      List<DetailImgFile> detailImgFiles,
      List<PreviewFile> previewFiles,
      SampleFile sampleFile) {
    this.author = author;
    this.approvalInfo = Objects.requireNonNullElse(approvalInfo, ApprovalInfo.init());
    this.bookConceptualInfo = bookConceptualInfo;
    this.bookPhysicalInfo = bookPhysicalInfo;
    this.bookPriceInfo = bookPriceInfo;
    this.publisher = publisher;
    this.stockCount = stockCount;
    this.coverImgFile = coverImgFile;
    this.detailImgFiles = detailImgFiles;
    this.previewFiles = previewFiles;
    this.sampleFile = sampleFile;
  }

  public String getCoverImgUrl() {
    return coverImgFile.getCoverImgUrl();
  }

  public List<String> getDetailImgUrls() {
    return detailImgFiles.stream().map(DetailImgFile::getDetailImgUrl).toList();
  }

  public List<String> getPreviewUrls() {
    return previewFiles.stream().map(PreviewFile::getPreviewUrl).toList();
  }

  public String getSampleUrl() {
    return sampleFile.getSampleUrl();
  }

  public Registration changeApprovalInfo(boolean approvalResult, String approvalMemo) {
    this.approvalInfo =
        ApprovalInfo.builder().approvalResult(approvalResult).approvalMemo(approvalMemo).build();
    return this;
  }

  public Registration changeRegistration(
      BookConceptualInfo bookConceptualInfo,
      BookPriceInfo bookPriceInfo,
      Long stockCount,
      CoverImgFile coverImgFile,
      List<DetailImgFile> detailImgFiles,
      List<PreviewFile> previewFiles,
      SampleFile sampleFile) {
    this.approvalInfo = ApprovalInfo.init();
    this.bookConceptualInfo = bookConceptualInfo;
    this.bookPriceInfo = bookPriceInfo;
    this.stockCount = stockCount;
    this.coverImgFile = coverImgFile.isNullFile() ? this.coverImgFile : coverImgFile;
    this.detailImgFiles = detailImgFiles.isEmpty() ? this.detailImgFiles : detailImgFiles;
    this.previewFiles = previewFiles.isEmpty() ? this.previewFiles : previewFiles;
    this.sampleFile = sampleFile.isNullFile() ? this.sampleFile : sampleFile;
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
