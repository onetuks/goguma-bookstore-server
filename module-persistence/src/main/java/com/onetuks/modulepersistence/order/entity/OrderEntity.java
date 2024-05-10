package com.onetuks.modulepersistence.order.entity;

import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulepersistence.order.vo.PaymentClient;
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
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

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
  public OrderEntity(
      MemberEntity memberEntity,
      String address,
      String addressDetail,
      Long deliveryFee,
      Long totalPrice,
      PaymentClient paymentClient,
      CashReceiptType cashReceiptType,
      String cashReceiptNumber,
      LocalDateTime orderedDate,
      LocalDateTime paymentDate) {
    this.memberEntity = memberEntity;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderEntity orderEntity = (OrderEntity) o;
    return Objects.equals(orderId, orderEntity.orderId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(orderId);
  }
}
