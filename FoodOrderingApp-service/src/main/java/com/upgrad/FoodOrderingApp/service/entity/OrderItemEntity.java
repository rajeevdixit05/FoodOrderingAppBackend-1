package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "order_item")
public class OrderItemEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "orderItemIdGenerator")
  @SequenceGenerator(
      name = "orderItemIdGenerator",
      sequenceName = "order_item_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  @NotNull
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private OrderEntity order;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "item_id", referencedColumnName = "id")
  @NotNull
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private ItemEntity item;

  @Column(name = "quantity")
  @NotNull
  private Integer quantity;

  @Column(name = "price")
  @NotNull
  private Integer price;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public OrderEntity getOrder() {
    return order;
  }

  public void setOrder(OrderEntity order) {
    this.order = order;
  }

  public ItemEntity getItem() {
    return item;
  }

  public void setItem(ItemEntity item) {
    this.item = item;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
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
