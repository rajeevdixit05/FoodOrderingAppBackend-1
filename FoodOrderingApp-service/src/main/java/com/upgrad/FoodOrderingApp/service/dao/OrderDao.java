package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

  @PersistenceContext EntityManager entityManager;

  /**
   * Method takes a Customer Id as input and list all orders made by the customer
   *
   * @param customerId Customer Id
   * @return List of OrderEntity
   */
  public List<OrderEntity> getOrdersForCustomer(final String customerId) {
    return entityManager
        .createNamedQuery("Orders.ByCustomer", OrderEntity.class)
        .setParameter("customerId", customerId)
        .getResultList();
  }

  public OrderEntity saveOrder(OrderEntity orderEntity) {
    entityManager.persist(orderEntity);
    return orderEntity;
  }

  public OrderItemEntity saveOrderItem(OrderItemEntity orderedItem) {
    entityManager.persist(orderedItem);
    return orderedItem;
  }

  public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurant) {
    try {
      return entityManager
          .createNamedQuery("fetchOrdersByRestaurant", OrderEntity.class)
          .setParameter("restaurant", restaurant)
          .getResultList();
    } catch (NoResultException nre) {
      System.out.printf("Ashik0 ");
      return null;
    }
  }
}
