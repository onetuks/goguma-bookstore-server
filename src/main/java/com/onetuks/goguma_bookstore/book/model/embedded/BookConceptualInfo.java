package com.onetuks.goguma_bookstore.book.model.embedded;

import com.onetuks.goguma_bookstore.book.model.converter.CategoryListToJsonConverter;
import com.onetuks.goguma_bookstore.book.vo.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.List;
import java.util.Objects;
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

  @Column(name = "isbn", nullable = false, unique = true)
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookConceptualInfo that = (BookConceptualInfo) o;
    return Objects.equals(title, that.title)
        && Objects.equals(oneLiner, that.oneLiner)
        && Objects.equals(summary, that.summary)
        && Objects.equals(categories, that.categories)
        && Objects.equals(isbn, that.isbn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, oneLiner, summary, categories, isbn);
  }
}
