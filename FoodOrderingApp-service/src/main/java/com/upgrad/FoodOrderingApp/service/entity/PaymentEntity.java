package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "payment")
@NamedQueries({
  @NamedQuery(name = "PaymentModes.All", query = "SELECT P FROM PaymentEntity P"),
  @NamedQuery(
      name = "PaymentModes.getById",
      query = "SELECT P FROM PaymentEntity P Where P.uuid=:uuid")
})
public class PaymentEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "paymentIdGenerator")
  @SequenceGenerator(
      name = "paymentIdGenerator",
      sequenceName = "payment_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "payment_name")
  @Size(max = 255)
  private String paymentName;

  public PaymentEntity() {}

  public PaymentEntity(String uuid, String paymentName) {
    this.uuid = uuid;
    this.paymentName = paymentName;
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

  public String getPaymentName() {
    return paymentName;
  }

  public void setPaymentName(String paymentName) {
    this.paymentName = paymentName;
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
