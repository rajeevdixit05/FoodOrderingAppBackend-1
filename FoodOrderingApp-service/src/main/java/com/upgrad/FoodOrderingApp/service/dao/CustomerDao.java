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
  private static final String CUSTOMER_BY_CONTACT_NUMBER = "customerByContactNumber";
  private static final String CUSTOMER_BY_PASSWORD = "customerByPassword";
  private static final String CUSTOMER_BY_ACCESS_TOKEN = "customerByAccessToken";
  @PersistenceContext private EntityManager entityManager;


  /**
   * This method is used for creating a new user.
   *
   * @param entity customer object
   * @return created customer Object
   */

  public CustomerEntity createUpdateUser(CustomerEntity entity) {
    entityManager.persist(entity);
    return entity;
  }

  /**
   * DAO method to check if a customer with  contactnumber exists
   *
   * @param contactNumber contactNumber of customer
   * @return CustomerEntity object or null if no found
   */
  public CustomerEntity checkContactNumber(String contactNumber) {

    try {
      return entityManager.createNamedQuery(CUSTOMER_BY_CONTACT_NUMBER, CustomerEntity.class).setParameter("contactNumber", contactNumber)
              .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }
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
   * DAO method to check if the password of the customer is valid.
   *
   * @param contactNumber contactnumber of customer
   * @param password      password of customer
   * @return CustomerEntity object or null if no found
   */
  public CustomerEntity checkPasswordisCorrect(String contactNumber, String password) {
    try {
      return entityManager.createNamedQuery(CUSTOMER_BY_PASSWORD, CustomerEntity.class).
              setParameter("contactNumber", contactNumber).
              setParameter("password", password)
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
   * method used to get customerentity object based on the Accesstoken provided
   *
   * @param accessToken accesstoken of the user
   * @return CustomerAuthEntity object
   */
  public CustomerAuthEntity getCustomerByAccessToken(String accessToken) {
    try {
      return entityManager.createNamedQuery(CUSTOMER_BY_ACCESS_TOKEN, CustomerAuthEntity.class).
              setParameter("accessToken", accessToken)
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

  /**
   * method used for updating logout date in the CustomerAuth entity object of the customer
   *
   * @param customerAuthEntity object to be updated.
   */
  public void updateLogOutDate(CustomerAuthEntity customerAuthEntity) {
    entityManager.persist(customerAuthEntity);
  }

  /**
   * DAO method to persist customerauth entity into database.
   *
   * @param authEntity customerauth entity object
   */
  public CustomerAuthEntity createAuthToken(CustomerAuthEntity authEntity) {
    entityManager.persist(authEntity);
    return authEntity;
  }
}
