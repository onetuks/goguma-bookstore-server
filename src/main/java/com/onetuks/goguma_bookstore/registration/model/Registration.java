package com.onetuks.goguma_bookstore.registration.model;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.book.model.converter.CategoryListToJsonConverter;
import com.onetuks.goguma_bookstore.book.model.converter.CustomFileListToJsonConverter;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.model.vo.PageSizeInfo;
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

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "one_liner", nullable = false)
  private String oneLiner;

  @Column(name = "summary", nullable = false)
  private String summary;

  @Convert(converter = CategoryListToJsonConverter.class)
  @Column(name = "categories", nullable = false)
  private List<Category> categories;

  @Column(name = "publisher", nullable = false)
  private String publisher;

  @Column(name = "isbn", nullable = false)
  private String isbn;

  @Embedded private PageSizeInfo pageSizeInfo;

  @Column(name = "cover_type", nullable = false)
  private String coverType;

  @Column(name = "page_count", nullable = false)
  private Long pageCount;

  @Column(name = "price", nullable = false)
  private Long price;

  @Column(name = "stock_count", nullable = false)
  private Long stockCount;

  @Column(name = "promotion", nullable = false)
  private Boolean promotion;

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
      String title,
      String oneLiner,
      String summary,
      List<Category> categories,
      String publisher,
      String isbn,
      PageSizeInfo pageSizeInfo,
      String coverType,
      Long pageCount,
      Long price,
      Long stockCount,
      Boolean promotion,
      CoverImgFile coverImgFile,
      List<DetailImgFile> detailImgFiles,
      List<PreviewFile> previewFiles,
      SampleFile sampleFile) {
    this.author = author;
    this.approvalInfo = Objects.requireNonNullElse(approvalInfo, ApprovalInfo.builder().build());
    this.title = title;
    this.oneLiner = oneLiner;
    this.summary = summary;
    this.categories = categories;
    this.publisher = publisher;
    this.isbn = isbn;
    this.pageSizeInfo = pageSizeInfo;
    this.coverType = coverType;
    this.pageCount = pageCount;
    this.price = price;
    this.stockCount = stockCount;
    this.promotion = promotion;
    this.coverImgFile = coverImgFile;
    this.detailImgFiles = detailImgFiles;
    this.previewFiles = previewFiles;
    this.sampleFile = sampleFile;
  }

  public boolean getApprovalResult() {
    return approvalInfo.getApprovalResult();
  }

  public String getApprovalMemo() {
    return approvalInfo.getApprovalMemo();
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

  public Registration updateApprovalInfo(boolean approvalResult, String approvalMemo) {
    this.approvalInfo =
        ApprovalInfo.builder().approvalResult(approvalResult).approvalMemo(approvalMemo).build();
    return this;
  }

  public Registration updateRegistration(
      String title,
      String oneLiner,
      String summary,
      List<Category> categories,
      String publisher,
      String isbn,
      PageSizeInfo pageSizeInfo,
      String coverType,
      Long pageCount,
      Long price,
      Long stockCount,
      Boolean promotion,
      CoverImgFile coverImgFile,
      List<DetailImgFile> detailImgFiles,
      List<PreviewFile> previewFiles,
      SampleFile sampleFile) {
    this.title = title;
    this.oneLiner = oneLiner;
    this.summary = summary;
    this.categories = categories;
    this.publisher = publisher;
    this.isbn = isbn;
    this.pageSizeInfo = pageSizeInfo;
    this.coverType = coverType;
    this.pageCount = pageCount;
    this.price = price;
    this.stockCount = stockCount;
    this.promotion = promotion;
    this.coverImgFile = coverImgFile;
    this.detailImgFiles = detailImgFiles;
    this.previewFiles = previewFiles;
    this.sampleFile = sampleFile;
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
