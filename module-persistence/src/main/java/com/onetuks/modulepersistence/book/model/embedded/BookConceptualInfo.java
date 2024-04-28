package com.onetuks.modulepersistence.book.model.embedded;

import com.onetuks.modulepersistence.book.vo.Category;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BookConceptualInfo {

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

  @Builder
  public BookConceptualInfo(
      String title, String oneLiner, String summary,
      List<Category> categories, String publisher, String isbn) {
    this.title = title;
    this.oneLiner = oneLiner;
    this.summary = summary;
    this.categories = categories;
    this.publisher = publisher;
    this.isbn = isbn;
  }

  public BookConceptualInfo changeBookConceptualInfo(
      String oneLiner, String summary, List<Category> categories) {
    this.oneLiner = oneLiner;
    this.summary = summary;
    this.categories = categories;
    return this;
  }
}
