package com.onetuks.dbstorage.restock.converter;

import com.onetuks.coredomain.restock.model.Restock;
import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.dbstorage.restock.entity.RestockEntity;
import org.springframework.stereotype.Component;

@Component
public class RestockConverter {

  private final MemberConverter memberConverter;
  private final BookConverter bookConverter;

  public RestockConverter(MemberConverter memberConverter, BookConverter bookConverter) {
    this.memberConverter = memberConverter;
    this.bookConverter = bookConverter;
  }

  public RestockEntity toEntity(Restock restock) {
    return new RestockEntity(
        restock.restockId(),
        memberConverter.toEntity(restock.member()),
        bookConverter.toEntity(restock.book()),
        restock.isFulfilled(),
        restock.isAlarmPermitted());
  }

  public Restock toDomain(RestockEntity restockEntity) {
    return new Restock(
        restockEntity.getRestockId(),
        memberConverter.toDomain(restockEntity.getMemberEntity()),
        bookConverter.toDomain(restockEntity.getBookEntity()),
        restockEntity.getIsFulfilled(),
        restockEntity.getIsAlarmPermitted());
  }
}
