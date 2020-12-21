package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_auth")
@NamedQueries({
  @NamedQuery(
      name = "Customer.ByAuthToken",
      query = "SELECT C FROM CustomerAuthEntity C WHERE C.accessToken = :accessToken")
})
public class CustomerAuthEntity implements Serializable {

  @Column(name = "login_at")
  LocalDateTime loginAt;

  @Column(name = "logout_at")
  LocalDateTime logoutAt;

  @Column(name = "expires_at")
  LocalDateTime expiresAt;

  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "customerAuthIdGenerator")
  @SequenceGenerator(
      name = "customerAuthIdGenerator",
      sequenceName = "customer_auth_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @Column(name = "uuid")
  @Size(max = 200)
  @NotNull
  private String uuid;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  @NotNull
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private CustomerEntity customer;

  @Column(name = "access_token")
  @Size(max = 500)
  @ToStringExclude
  @HashCodeExclude
  private String accessToken;

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

  public CustomerEntity getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerEntity customer) {
    this.customer = customer;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public LocalDateTime getLoginAt() {
    return loginAt;
  }

  public void setLoginAt(LocalDateTime loginAt) {
    this.loginAt = loginAt;
  }

  public LocalDateTime getLogoutAt() {
    return logoutAt;
  }

  public void setLogoutAt(LocalDateTime logoutAt) {
    this.logoutAt = logoutAt;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
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
