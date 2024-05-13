package com.onetuks.coredomain.registration.model;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.book.model.vo.BookConceptualInfo;
import com.onetuks.coredomain.book.model.vo.BookPhysicalInfo;
import com.onetuks.coredomain.book.model.vo.BookPriceInfo;
import com.onetuks.coredomain.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.file.filepath.PreviewFilePath.PreviewFilePaths;
import com.onetuks.coredomain.file.filepath.SampleFilePath;
import com.onetuks.coredomain.registration.model.vo.ApprovalInfo;
import com.onetuks.coreobj.enums.book.Category;
import java.util.List;

public record Registration(
    Long registrationId,
    Author author,
    ApprovalInfo approvalInfo,
    BookConceptualInfo bookConceptualInfo,
    BookPhysicalInfo bookPhysicalInfo,
    BookPriceInfo bookPriceInfo,
    CoverImgFilePath coverImgFilePath,
    DetailImgFilePaths detailImgFilePaths,
    PreviewFilePaths previewFilePaths,
    SampleFilePath sampleFilePath
) {

  public Registration changeApprovalInfo(boolean isApproved, String approvalMemo) {
    return new Registration(
        registrationId(),
        author(),
        new ApprovalInfo(isApproved, approvalMemo),
        bookConceptualInfo(),
        bookPhysicalInfo(),
        bookPriceInfo(),
        coverImgFilePath(),
        detailImgFilePaths(),
        previewFilePaths(),
        sampleFilePath()
    );
  }

  public Registration changeRegistration(
      String oneLiner, String summary, List<Category> categories,
      long price, int salesRate, boolean isPromotion, long stockCount,
      CoverImgFilePath coverImgFilePath, DetailImgFilePaths detailImgFilePaths,
      PreviewFilePaths previewFilePaths, SampleFilePath sampleFilePath) {
    return new Registration(
        registrationId(),
        author(),
        approvalInfo(),
        new BookConceptualInfo(
            bookConceptualInfo().title(),
            oneLiner,
            summary,
            categories,
            bookConceptualInfo().publisher(),
            bookConceptualInfo().isbn()
        ),
        bookPhysicalInfo(),
        new BookPriceInfo(
            price,
            salesRate,
            isPromotion,
            stockCount
        ),
        coverImgFilePath,
        detailImgFilePaths,
        previewFilePaths,
        sampleFilePath);
  }

  public Registration changeRegistration(
      String oneLiner, String summary, List<Category> categories,
      long price, int salesRate, boolean isPromotion, long stockCount,
      CoverImgFilePath coverImgFilePath,
      DetailImgFilePaths detailImgFilePaths,
      PreviewFilePaths previewFilePaths) {
    return new Registration(
        registrationId(),
        author(),
        approvalInfo(),
        new BookConceptualInfo(
            bookConceptualInfo().title(),
            oneLiner,
            summary,
            categories,
            bookConceptualInfo().publisher(),
            bookConceptualInfo().isbn()
        ),
        bookPhysicalInfo(),
        new BookPriceInfo(
            price,
            salesRate,
            isPromotion,
            stockCount
        ),
        coverImgFilePath,
        detailImgFilePaths,
        previewFilePaths,
        sampleFilePath());
  }
}
