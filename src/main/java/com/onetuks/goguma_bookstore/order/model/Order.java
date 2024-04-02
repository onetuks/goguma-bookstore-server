package com.onetuks.goguma_bookstore.order.model;

import com.onetuks.goguma_bookstore.auth.model.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "address_detail", nullable = false)
  private String addressDetail;

  @Column(name = "delivery_fee", nullable = false)
  private Long deliveryFee;

  @Column(name = "total_price", nullable = false)
  private Long totalPrice;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "payment_client", nullable = false)
  private PaymentClient paymentClient;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "cash_receipt_type", nullable = false)
  private CashReceiptType cashReceiptType;

  @Column(name = "cash_receipt_number", nullable = false)
  private String cashReceiptNumber;

  @Column(name = "ordered_date", nullable = false)
  private LocalDateTime orderedDate;

  @Column(name = "payment_date", nullable = false)
  private LocalDateTime paymentDate;

  @Builder
  public Order(
      Member member,
      String address,
      String addressDetail,
      Long deliveryFee,
      Long totalPrice,
      PaymentClient paymentClient,
      CashReceiptType cashReceiptType,
      String cashReceiptNumber,
      LocalDateTime orderedDate,
      LocalDateTime paymentDate) {
    this.member = member;
    this.address = address;
    this.addressDetail = addressDetail;
    this.deliveryFee = deliveryFee;
    this.totalPrice = totalPrice;
    this.paymentClient = paymentClient;
    this.cashReceiptType = cashReceiptType;
    this.cashReceiptNumber = cashReceiptNumber;
    this.orderedDate = orderedDate;
    this.paymentDate = paymentDate;
  }
}
