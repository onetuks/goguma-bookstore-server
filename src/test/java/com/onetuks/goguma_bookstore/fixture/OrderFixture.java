package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.order.model.Order;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import com.onetuks.goguma_bookstore.order.vo.PaymentClient;
import java.time.LocalDateTime;

public class OrderFixture {

  public static Order create(Member member, Book book) {
    return Order.builder()
        .member(member)
        .address("경기도 용인시")
        .addressDetail("수지구 풍덕천동")
        .deliveryFee(3_500L)
        .totalPrice(book.getBookPriceInfo().getPurchasePrice() + 3_500L)
        .paymentClient(PaymentClient.KAKAO)
        .cashReceiptType(CashReceiptType.PERSON)
        .cashReceiptNumber("1234-1234")
        .orderedDate(LocalDateTime.now())
        .paymentDate(LocalDateTime.now())
        .build();
  }
}
