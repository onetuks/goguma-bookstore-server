package com.onetuks.dbstorage.registration.entity;

import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

  @Column(name = "is_approved", nullable = false)
  private Boolean isApproved;

  @Column(name = "approval_memo", nullable = false)
  private String approvalMemo;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "one_liner", nullable = false)
  private String oneLiner;

  @Column(name = "summary", nullable = false)
  private String summary;

  @Type(JsonType.class)
  @Column(name = "categories", nullable = false)
  private List<Category> categories;

  @Column(name = "publisher", nullable = false)
  private String publisher;

  @Column(name = "isbn", nullable = false, unique = true)
  private String isbn;

  @Column(name = "height", nullable = false)
  private Integer height;

  @Column(name = "width", nullable = false)
  private Integer width;

  @Column(name = "cover_type", nullable = false)
  private String coverType;

  @Column(name = "page_count", nullable = false)
  private Long pageCount;

  @Column(name = "price", nullable = false)
  private Long price;

  @Column(name = "sales_rate", nullable = false)
  private Integer salesRate;

  @Column(name = "is_promotion", nullable = false)
  private Boolean isPromotion;

  @Column(name = "stock_count", nullable = false)
  private Long stockCount;

  @Column(name = "cover_img_uri")
  private String coverImgUri;

  @Type(JsonType.class)
  @Column(name = "detail_img_uris")
  private List<String> detailImgUris;

  @Type(JsonType.class)
  @Column(name = "preview_uris")
  private List<String> previewUris;

  @Column(name = "sample_uri")
  private String sampleUri;

  public RegistrationEntity(
      Long registrationId,
      AuthorEntity authorEntity,
      Boolean isApproved,
      String approvalMemo,
      String title,
      String oneLiner,
      String summary,
      List<Category> categories,
      String publisher,
      String isbn,
      Integer height,
      Integer width,
      String coverType,
      Long pageCount,
      Long price,
      Integer salesRate,
      Boolean isPromotion,
      Long stockCount,
      String coverImgUri,
      List<String> detailImgUris,
      List<String> previewUris,
      String sampleUri) {
    this.registrationId = registrationId;
    this.authorEntity = authorEntity;
    this.isApproved = isApproved;
    this.approvalMemo = approvalMemo;
    this.title = title;
    this.oneLiner = oneLiner;
    this.summary = summary;
    this.categories = categories;
    this.publisher = publisher;
    this.isbn = isbn;
    this.height = height;
    this.width = width;
    this.coverType = coverType;
    this.pageCount = pageCount;
    this.price = price;
    this.salesRate = salesRate;
    this.isPromotion = isPromotion;
    this.stockCount = stockCount;
    this.coverImgUri = coverImgUri;
    this.detailImgUris = detailImgUris;
    this.previewUris = previewUris;
    this.sampleUri = sampleUri;
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
