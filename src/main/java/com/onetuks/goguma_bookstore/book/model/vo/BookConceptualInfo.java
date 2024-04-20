package com.onetuks.goguma_bookstore.book.model.vo;

import com.onetuks.goguma_bookstore.book.model.converter.CategoryListToJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BookConceptualInfo {

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "one_liner", nullable = false)
  private String oneLiner;

  @Column(name = "summary", nullable = false)
  private String summary;

  @Convert(converter = CategoryListToJsonConverter.class)
  @Column(name = "categories", nullable = false)
  private List<Category> categories;

  @Column(name = "isbn", nullable = false)
  private String isbn;

  @Builder
  public BookConceptualInfo(
      String title, String oneLiner, String summary, List<Category> categories, String isbn) {
    this.title = title;
    this.oneLiner = oneLiner;
    this.summary = summary;
    this.categories = categories;
    this.isbn = isbn;
  }

  public BookConceptualInfo updateBookConceptualInfo(
      String title, String oneLiner, String summary, List<Category> categories, String isbn) {
    this.title = title;
    this.oneLiner = oneLiner;
    this.summary = summary;
    this.categories = categories;
    this.isbn = isbn;
    return this;
  }
}
