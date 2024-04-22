package com.onetuks.goguma_bookstore.book.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BookPhysicalInfo {

  @Column(name = "height", nullable = false)
  private Integer height;

  @Column(name = "width", nullable = false)
  private Integer width;

  @Column(name = "cover_type", nullable = false)
  private String coverType;

  @Column(name = "page_count", nullable = false)
  private Long pageCount;

  @Builder
  public BookPhysicalInfo(Integer height, Integer width, String coverType, Long pageCount) {
    this.height = height;
    this.width = width;
    this.coverType = coverType;
    this.pageCount = pageCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookPhysicalInfo that = (BookPhysicalInfo) o;
    return Objects.equals(height, that.height)
        && Objects.equals(width, that.width)
        && Objects.equals(coverType, that.coverType)
        && Objects.equals(pageCount, that.pageCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(height, width, coverType, pageCount);
  }
}
