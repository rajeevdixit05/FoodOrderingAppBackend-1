package com.upgrad.FoodOrderingApp.service.entity;

import com.upgrad.FoodOrderingApp.service.common.ItemType;
import org.apache.commons.lang3.builder.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@NamedQueries({
  @NamedQuery(
      name = "ItemEntity.getItemById",
      query = "SELECT i FROM ItemEntity i WHERE i.uuid=:uuid")
})
public class ItemEntity implements Serializable {
  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  @NotNull
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  Set<OrderItemEntity> orders = new HashSet<OrderItemEntity>();

  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "itemIdGenerator")
  @SequenceGenerator(
      name = "itemIdGenerator",
      sequenceName = "item_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private Integer id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "item_name")
  @NotNull
  @Size(max = 30)
  private String itemName;

  @Column(name = "price")
  @NotNull
  private Integer price;

  @Column(name = "type")
  @Size(max = 10)
  @NotNull
  private ItemType type;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "category_item",
      joinColumns = {@JoinColumn(name = "item_id")},
      inverseJoinColumns = {@JoinColumn(name = "category_id")})
  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  private Set<CategoryEntity> categories = new HashSet<CategoryEntity>();

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

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public ItemType getType() {
    return type;
  }

  public void setType(ItemType type) {
    this.type = type;
  }

  public Set<CategoryEntity> getCategories() {
    return categories;
  }

  public void setCategories(Set<CategoryEntity> categories) {
    this.categories = categories;
  }

  public Set<OrderItemEntity> getOrders() {
    return orders;
  }

  public void setOrders(Set<OrderItemEntity> orders) {
    this.orders = orders;
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
