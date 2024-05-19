package com.onetuks.coredomain.book.model.vo;

import com.onetuks.coreobj.enums.book.Category;
import java.util.List;

public record BookConceptualInfo(
    String title,
    String oneLiner,
    String summary,
    List<Category> categories,
    String publisher,
    String isbn) {}
