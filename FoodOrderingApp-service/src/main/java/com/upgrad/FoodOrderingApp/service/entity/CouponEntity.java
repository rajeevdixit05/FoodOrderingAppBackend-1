package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "coupon")
@NamedQueries({
  @NamedQuery(
      name = "Coupon.ByName",
      query = "SELECT C FROM CouponEntity C WHERE C.couponName = :couponName"),
  @NamedQuery(name = "Coupon.ByUuid", query = "SELECT C FROM CouponEntity C WHERE C.uuid = :uuid")
})
public class CouponEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "couponIdGenerator")
  @SequenceGenerator(
      name = "couponIdGenerator",
      sequenceName = "coupon_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "coupon_name")
  @Size(max = 255)
  private String couponName;

  @Column(name = "percent")
  @NotNull
  private Integer percent;

  public CouponEntity() {}

  public CouponEntity(final String uuid, final String couponName, Integer percent) {
    this.uuid = uuid;
    this.couponName = couponName;
    this.percent = percent;
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

  public String getCouponName() {
    return couponName;
  }

  public void setCouponName(String couponName) {
    this.couponName = couponName;
  }

  public Integer getPercent() {
    return percent;
  }

  public void setPercent(Integer percent) {
    this.percent = percent;
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
