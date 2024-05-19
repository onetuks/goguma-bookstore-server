package com.onetuks.scmapi.registration.dto.response;

import com.onetuks.scmdomain.verification.webclient.dto.result.RegistrationIsbnResult;
import com.onetuks.scmdomain.verification.webclient.dto.result.RegistrationIsbnResult.IsbnDataResult;

public record RegistrationIsbnGetResponse(
    String isbn,
    String title,
    String publisher,
    String coverType,
    Long pageCount,
    Integer height,
    Integer width) {

  public static RegistrationIsbnGetResponse from(RegistrationIsbnResult result) {
    if (result == null || result.docs().isEmpty()) {
      throw new IllegalArgumentException("해당 ISBN 정보가 존재하지 않습니다.");
    }

    IsbnDataResult data = result.docs().get(0);

    Integer[] bookSizeInfo = parseBookSizeInfo(data.BOOK_SIZE());

    return new RegistrationIsbnGetResponse(
        data.EA_ISBN(),
        data.TITLE(),
        data.PUBLISHER(),
        data.FORM_DETAIL(),
        parsePageCountInfo(data.PAGE()),
        bookSizeInfo[1],
        bookSizeInfo[0]);
  }

  private static Long parsePageCountInfo(String pageCount) {
    try {
      return Long.parseLong(pageCount.replaceAll("\\D", ""));
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private static Integer[] parseBookSizeInfo(String bookSize) {
    try {
      // bookSize : "width*height"
      String[] bookSizeInfo = bookSize.split("x");
      return new Integer[] {Integer.parseInt(bookSizeInfo[0]), Integer.parseInt(bookSizeInfo[1])};
    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
      return new Integer[] {null, null};
    }
  }
}
