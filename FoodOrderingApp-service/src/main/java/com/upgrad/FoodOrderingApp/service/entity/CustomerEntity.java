package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customer")
@NamedQueries({
  @NamedQuery(
      name = "Customer.ByContact",
      query = "SELECT C FROM CustomerEntity C WHERE C.contactNumber = :contactNumber")
})
public class CustomerEntity implements Serializable {

  @Id
  @GeneratedValue(generator = "customerIdGenerator")
  @SequenceGenerator(
      name = "customerIdGenerator",
      sequenceName = "customer_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @Column(name = "id")
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @Column(name = "uuid")
  @Size(max = 200)
  @NotNull
  private String uuid;

  @Column(name = "firstname")
  @NotNull
  @Size(max = 30)
  private String firstName;

  @Column(name = "lastname")
  @Size(max = 30)
  private String lastName;

  @Column(name = "email")
  @Size(max = 50)
  private String email;

  @Column(name = "contact_number")
  @Size(max = 30)
  @NotNull
  private String contactNumber;

  @Column(name = "password")
  @Size(max = 255)
  @NotNull
  @ToStringExclude
  @HashCodeExclude
  private String password;

  @Column(name = "salt")
  @Size(max = 255)
  @NotNull
  @ToStringExclude
  @HashCodeExclude
  private String salt;

  @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
  private List<AddressEntity> addresses;

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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public List<AddressEntity> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<AddressEntity> addresses) {
    this.addresses = addresses;
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
