package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "state")
@NamedQueries({
  @NamedQuery(
      name = "fetchStateByUUID",
      query = "SELECT s from StateEntity s WHERE  s.uuid = :uuid"),
  @NamedQuery(name = "fetchAllStates", query = "SELECT s from StateEntity s")
})
public class StateEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "stateIdGenerator")
  @SequenceGenerator(
      name = "stateIdGenerator",
      sequenceName = "state_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "state_name")
  @Size(max = 30)
  private String stateName;

  public StateEntity() {}

  public StateEntity(@NotNull @Size(max = 200) String uuid, @Size(max = 30) String stateName) {
    this.uuid = uuid;
    this.stateName = stateName;
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

  public String getStateName() {
    return stateName;
  }

  public void setStateName(String stateName) {
    this.stateName = stateName;
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
