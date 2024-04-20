package com.onetuks.goguma_bookstore.book.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PageSizeInfo {

  @Column(name = "height", nullable = false)
  private Integer height;

  @Column(name = "width", nullable = false)
  private Integer width;

  public PageSizeInfo(Integer height, Integer width) {
    this.height = height;
    this.width = width;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageSizeInfo that = (PageSizeInfo) o;
    return Objects.equals(height, that.height) && Objects.equals(width, that.width);
  }

  @Override
  public int hashCode() {
    return Objects.hash(height, width);
  }
}
