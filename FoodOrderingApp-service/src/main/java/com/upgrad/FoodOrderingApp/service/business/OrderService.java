package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.CPF_001;
import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.CPF_002;

@Service
public class OrderService {

  @Autowired CouponDao couponDao;

  @Autowired OrderDao orderDao;

  /**
   * Method takes a coupon name and returns details of the matching coupon
   *
   * @param couponName
   * @return CouponEntity with Coupon Details
   * @throws CouponNotFoundException on no input (coupon name) and no such coupon is found
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public CouponEntity getCouponByCouponName(final String couponName)
      throws CouponNotFoundException {
    // Check is input coupon name is valid
    if ((couponName == null) || (couponName.isEmpty())) {
      // Throw error if coupon name is empty
      throw new CouponNotFoundException(CPF_002.getCode(), CPF_002.getDefaultMessage());
    }

    // Fetch matching CouponEntity from the database
    final CouponEntity couponEntity = couponDao.getCouponByCouponName(couponName);

    if (couponEntity != null) {
      return couponEntity;
    } else {
      // Throw error if not such coupon is found
      throw new CouponNotFoundException(CPF_001.getCode(), CPF_001.getDefaultMessage());
    }
  }

  public CouponEntity getCouponByCouponId(String uuid) throws CouponNotFoundException {
    CouponEntity couponEntity = couponDao.getCouponByCouponId(uuid);
    if (couponEntity != null) {
      return couponEntity;
    } else throw new CouponNotFoundException("CPF-002", "No coupon by this id");
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public OrderEntity saveOrder(OrderEntity orderEntity) {
    return orderDao.saveOrder(orderEntity);
  }

  /**
   * Method takes a customer id in the input and returns all of the customer's orders
   *
   * @param customerId Customer Id
   * @return List of all of the customer's order. Empty if customer has not placed any orders.
   */
  public List<OrderEntity> getOrdersByCustomers(final String customerId) {
    return orderDao.getOrdersForCustomer(customerId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public OrderItemEntity saveOrderItem(OrderItemEntity orderedItem) {
    return orderDao.saveOrderItem(orderedItem);
  }
}
