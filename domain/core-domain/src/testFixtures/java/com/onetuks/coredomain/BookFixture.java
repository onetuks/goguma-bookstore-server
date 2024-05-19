package com.onetuks.coredomain;

import static com.onetuks.coredomain.CustomFilePathFixture.createCoverImgFilePath;
import static com.onetuks.coredomain.CustomFilePathFixture.createDetailImgFilePaths;
import static com.onetuks.coredomain.CustomFilePathFixture.createPreviewFilePaths;
import static com.onetuks.coredomain.util.TestValueProvider.createBookConceptualInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createBookPhysicalInfo;
import static com.onetuks.coredomain.util.TestValueProvider.createBookPriceInfo;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.model.BookStatics;
import com.onetuks.coreobj.file.UUIDProvider;

public class BookFixture {

  public static Book create(Long bookId, Author author) {
    String uuid = UUIDProvider.provideUUID();
    return new Book(
        bookId,
        author,
        createBookConceptualInfo(),
        createBookPhysicalInfo(),
        createBookPriceInfo(),
        createCoverImgFilePath(uuid),
        createDetailImgFilePaths(uuid),
        createPreviewFilePaths(uuid),
        BookStatics.init());
  }
}
