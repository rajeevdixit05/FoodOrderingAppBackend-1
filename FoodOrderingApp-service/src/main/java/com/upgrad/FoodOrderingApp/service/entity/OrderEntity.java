package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@NamedQueries({
  @NamedQuery(
      name = "Orders.ByCustomer",
      query =
          "SELECT O FROM OrderEntity O WHERE O.customer.uuid = :customerId ORDER BY O.date DESC"),
  @NamedQuery(
      name = "fetchOrdersByRestaurant",
      query = "SELECT o FROM OrderEntity o where o.restaurant = :restaurant")
})
public class OrderEntity implements Serializable {
  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  Set<OrderItemEntity> items = new HashSet<OrderItemEntity>();

  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "orderIdGenerator")
  @SequenceGenerator(
      name = "orderIdGenerator",
      sequenceName = "orders_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "bill")
  @NotNull
  private Double bill;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "coupon_id", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private CouponEntity coupon;

  @Column(name = "discount")
  private Double discount;

  @Column(name = "date")
  @NotNull
  private LocalDateTime date;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "payment_id", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private PaymentEntity payment;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @NotNull
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private CustomerEntity customer;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @NotNull
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private AddressEntity address;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @NotNull
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private RestaurantEntity restaurant;

  public OrderEntity() {}

  public OrderEntity(
      String uuid,
      double bill,
      CouponEntity coupon,
      double discount,
      Date date,
      PaymentEntity payment,
      CustomerEntity customer,
      AddressEntity address,
      RestaurantEntity restaurant) {
    this.uuid = uuid;
    this.date =
        Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    this.bill = bill;
    this.coupon = coupon;
    this.discount = discount;
    this.restaurant = restaurant;
    this.customer = customer;
    this.address = address;
    this.payment = payment;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Double getBill() {
    return bill;
  }

  public void setBill(Double bill) {
    this.bill = bill;
  }

  public CouponEntity getCoupon() {
    return coupon;
  }

  public void setCoupon(CouponEntity coupon) {
    this.coupon = coupon;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public PaymentEntity getPayment() {
    return payment;
  }

  public void setPayment(PaymentEntity payment) {
    this.payment = payment;
  }

  public CustomerEntity getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerEntity customer) {
    this.customer = customer;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
  }

  public RestaurantEntity getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(RestaurantEntity restaurant) {
    this.restaurant = restaurant;
  }

  public Set<OrderItemEntity> getItems() {
    return items;
  }

  public void setItems(Set<OrderItemEntity> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, Boolean.FALSE);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, Boolean.FALSE);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
