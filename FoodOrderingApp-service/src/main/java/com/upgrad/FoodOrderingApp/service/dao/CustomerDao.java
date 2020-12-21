package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

  @PersistenceContext private EntityManager entityManager;

  /**
   * Method takes a CustomerEntity and stores it in the database
   *
   * @param customerEntity CustomerEntity to persist
   * @return persisted CustomerEntity
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public CustomerEntity saveCustomer(final CustomerEntity customerEntity) {
    entityManager.persist(customerEntity);
    System.out.println(customerEntity.toString());
    return customerEntity;
  }

  /**
   * Method takes a CustomerAuthEntity and stores it in the database
   *
   * @param customerAuthEntity CustomerAuthEntity to persist
   * @return persisted CustomerAuthEntity
   */
  public CustomerAuthEntity saveCustomerAuthentication(
      final CustomerAuthEntity customerAuthEntity) {
    entityManager.persist(customerAuthEntity);
    return customerAuthEntity;
  }

  /**
   * Method takes a contact number and returns the matching CustomerEntity
   *
   * @param contactNumber contact number
   * @return CustomerEntity
   */
  public CustomerEntity getCustomerByContactNumber(final String contactNumber) {
    try {
      return entityManager
          .createNamedQuery("Customer.ByContact", CustomerEntity.class)
          .setParameter("contactNumber", contactNumber)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Method takes an access token as a parameter and returns the matching CustomerAuthEntity
   *
   * @param accessToken access token
   * @return CustomerAuthEntity
   */
  public CustomerAuthEntity getCustomerAuthenticationByAccessToken(String accessToken) {
    try {
      return entityManager
          .createNamedQuery("Customer.ByAuthToken", CustomerAuthEntity.class)
          .setParameter("accessToken", accessToken)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Method takes an updated CustomerEntity and merges the updates to the database
   *
   * @param customerEntity Updated CustomerEntity
   * @return persisted CustomerEntity
   */
  public CustomerEntity updateCustomer(final CustomerEntity customerEntity) {
    return entityManager.merge(customerEntity);
  }
}
