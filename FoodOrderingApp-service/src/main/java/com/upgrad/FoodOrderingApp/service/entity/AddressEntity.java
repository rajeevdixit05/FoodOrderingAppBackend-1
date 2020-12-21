package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@NamedQueries(
    @NamedQuery(
        name = "fetchAddressById",
        query = "SELECT a FROM AddressEntity a WHERE a.uuid=:addressId and a.active = 1"))
public class AddressEntity implements Serializable, Comparable<AddressEntity> {
  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "addressIdGenerator")
  @SequenceGenerator(
      name = "addressIdGenerator",
      sequenceName = "address_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "flat_buil_number")
  @Size(max = 255)
  private String flatBuilNo;

  @Column(name = "locality")
  @Size(max = 255)
  private String locality;

  @Column(name = "city")
  @Size(max = 30)
  private String city;

  @Column(name = "pincode")
  @Size(max = 30)
  private String pincode;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "state_id", referencedColumnName = "id")
  private StateEntity state;

  @Column(name = "active")
  private Integer active;

  @ManyToOne
  @JoinTable(
      name = "customer_address",
      joinColumns = {@JoinColumn(name = "address_id")},
      inverseJoinColumns = {@JoinColumn(name = "customer_id")})
  private CustomerEntity customer;

  @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
  private List<OrderEntity> orders = new ArrayList<>();

  public AddressEntity(
      String uuid,
      String flatBuilNo,
      String locality,
      String city,
      String pincode,
      StateEntity state) {
    this.uuid = uuid;
    this.flatBuilNo = flatBuilNo;
    this.locality = locality;
    this.city = city;
    this.pincode = pincode;
    this.state = state;
  }

  public AddressEntity() {}

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

  public String getFlatBuilNo() {
    return flatBuilNo;
  }

  public void setFlatBuilNo(String flatBuilNo) {
    this.flatBuilNo = flatBuilNo;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  public StateEntity getState() {
    return state;
  }

  public void setState(StateEntity state) {
    this.state = state;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public CustomerEntity getCustomers() {
    return customer;
  }

  public void setCustomers(CustomerEntity customer) {
    this.customer = customer;
  }

  public List<OrderEntity> getOrders() {
    return orders;
  }

  public void setOrders(List<OrderEntity> orders) {
    this.orders = orders;
  }

  @Override
  public int compareTo(AddressEntity i) {
    return this.getId().compareTo(i.getId());
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
