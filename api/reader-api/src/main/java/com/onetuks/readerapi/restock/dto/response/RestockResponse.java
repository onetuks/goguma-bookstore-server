package com.onetuks.readerapi.restock.dto.response;

import com.onetuks.coredomain.restock.model.Restock;
import org.springframework.data.domain.Page;

public record RestockResponse(
    long restockId,
    long memberId,
    long bookId,
    boolean isFulfilled,
    boolean isAlarmPermitted
) {

  public static RestockResponse from(Restock restock) {
    return new RestockResponse(
        restock.restockId(),
        restock.member().memberId(),
        restock.book().bookId(),
        restock.isFulfilled(),
        restock.isAlarmPermitted()
    );
  }

  public record RestockResponses(Page<RestockResponse> responses) {

    public static RestockResponses from(Page<Restock> restocks) {
      return new RestockResponses(restocks.map(RestockResponse::from));
    }
  }
}
