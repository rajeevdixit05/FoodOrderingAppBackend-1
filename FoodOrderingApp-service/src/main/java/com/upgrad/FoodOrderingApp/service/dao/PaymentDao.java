package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PaymentDao {

  @PersistenceContext EntityManager entityManager;

  /**
   * Method returns all available Payment Entities in the system
   *
   * @return List of PaymentEntity
   */
  public List<PaymentEntity> getAllPaymentMethods() {
    return entityManager.createNamedQuery("PaymentModes.All", PaymentEntity.class).getResultList();
  }

  public PaymentEntity getPaymentByUUID(String uuid) {
    try {
      return entityManager
          .createNamedQuery("PaymentModes.getById", PaymentEntity.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }
}
