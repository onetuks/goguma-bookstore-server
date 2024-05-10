package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulepersistence.book.entity.BookEntity;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.order.entity.OrderEntity;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulepersistence.order.vo.PaymentClient;
import java.time.LocalDateTime;

public class OrderFixture {

  public static OrderEntity create(MemberEntity memberEntity, BookEntity bookEntity) {
    return OrderEntity.builder()
        .member(memberEntity)
        .address("경기도 용인시")
        .addressDetail("수지구 풍덕천동")
        .deliveryFee(3_500L)
        .totalPrice(bookEntity.getBookPriceInfo().getPurchasePrice() + 3_500L)
        .paymentClient(PaymentClient.KAKAO)
        .cashReceiptType(CashReceiptType.PERSON)
        .cashReceiptNumber("1234-1234")
        .orderedDate(LocalDateTime.now())
        .paymentDate(LocalDateTime.now())
        .build();
  }
}
