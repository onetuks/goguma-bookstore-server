package com.onetuks.coredomain.book.model;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.vo.BookConceptualInfo;
import com.onetuks.coredomain.book.model.vo.BookPhysicalInfo;
import com.onetuks.coredomain.book.model.vo.BookPriceInfo;
import com.onetuks.coredomain.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.file.filepath.PreviewFilePath.PreviewFilePaths;

public record Book(
    Long bookId,
    Author author,
    BookConceptualInfo bookConceptualInfo,
    BookPhysicalInfo bookPhysicalInfo,
    BookPriceInfo bookPriceInfo,
    CoverImgFilePath coverImgFilePath,
    DetailImgFilePaths detailImgFilePaths,
    PreviewFilePaths previewFilePaths,
    BookStatics bookStatics) {

  public Book changeStockCount(long newStockCount) {
    return new Book(
        bookId(),
        author(),
        bookConceptualInfo(),
        bookPhysicalInfo(),
        new BookPriceInfo(
            bookPriceInfo().price(),
            bookPriceInfo().salesRate(),
            bookPriceInfo().isPromotion(),
            newStockCount),
        coverImgFilePath(),
        detailImgFilePaths(),
        previewFilePaths(),
        bookStatics());
  }
}
