package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.PNF_002;

@Service
public class PaymentService {

  @Autowired PaymentDao paymentDao;

  /**
   * Method returns all available payment methods in the system
   *
   * @return List of all available payment methods
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public List<PaymentEntity> getAllPaymentMethods() {
    return paymentDao.getAllPaymentMethods();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public PaymentEntity getPaymentByUUID(String paymentID) throws PaymentMethodNotFoundException {
    PaymentEntity paymentEntity = paymentDao.getPaymentByUUID(paymentID);
    if (paymentEntity == null) {
      throw new PaymentMethodNotFoundException(PNF_002.getCode(), PNF_002.getDefaultMessage());
    } else return paymentEntity;
  }
}
