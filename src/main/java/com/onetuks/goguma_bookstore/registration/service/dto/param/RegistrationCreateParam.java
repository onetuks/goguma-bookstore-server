package com.onetuks.goguma_bookstore.registration.service.dto.param;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.model.vo.PageSizeInfo;
import java.util.List;

public record RegistrationCreateParam(
    String title,
    String oneLiner,
    String summary,
    List<Category> categories,
    String publisher,
    String isbn,
    PageSizeInfo pageSizeInfo,
    String coverType,
    long pageCount,
    long price,
    long stockCount,
    boolean promotion) {}
