package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "restaurant_category")
@NamedQueries({
  @NamedQuery(
      name = "RestaurantCategoryEntity.getCategoryByRestaurant",
      query =
          "SELECT rc.categoryEntity from RestaurantCategoryEntity rc WHERE rc.restaurantEntity=:restaurant"),
  @NamedQuery(
      name = "RestaurantCategoryEntity.getRestaurantByCategory",
      query =
          "SELECT rc.restaurantEntity from RestaurantCategoryEntity rc WHERE rc.categoryEntity=:category")
})
public class RestaurantCategoryEntity implements Serializable {
  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "restaurantCategoryIdGenerator")
  @SequenceGenerator(
      name = "restaurantCategoryIdGenerator",
      sequenceName = "restaurant_category_id_seq",
      initialValue = 1,
      allocationSize = 1)
  @ToStringExclude
  @HashCodeExclude
  private int id;

  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
  private RestaurantEntity restaurantEntity;

  @ToStringExclude
  @HashCodeExclude
  @EqualsExclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private CategoryEntity categoryEntity;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public RestaurantEntity getRestaurantEntity() {
    return restaurantEntity;
  }

  public void setRestaurantEntity(RestaurantEntity restaurantEntity) {
    this.restaurantEntity = restaurantEntity;
  }

  public CategoryEntity getCategoryEntity() {
    return categoryEntity;
  }

  public void setCategoryEntity(CategoryEntity categoryEntity) {
    this.categoryEntity = categoryEntity;
  }
}
