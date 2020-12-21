package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {

  @PersistenceContext private EntityManager entityManager;

  /**
   * Method takes a AddressEntity and stores it in the database
   *
   * @param address AddressEntity to persist
   * @return persisted AddressEntity
   */
  public AddressEntity saveAddress(final AddressEntity address) {
    entityManager.persist(address);
    return address;
  }

  /**
   * Method takes a addressId and returns the matching AddressEntity
   *
   * @param addressId address uuid
   * @return AddressEntity
   */
  public AddressEntity getAddressByAddressId(final String addressId) {
    try {
      return entityManager
          .createNamedQuery("fetchAddressById", AddressEntity.class)
          .setParameter("addressId", addressId)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Method takes a AddressEntity and deletes the matching AddressEntity
   *
   * @param address AddressEntity
   * @return AddressEntity
   */
  public AddressEntity deleteAddress(final AddressEntity address) {
    entityManager.remove(address);
    return address;
  }

  /**
   * Method takes a AddressEntity and soft deletes the matching AddressEntity
   *
   * @param address AddressEntity
   * @return AddressEntity
   */
  public AddressEntity deactivateAddress(final AddressEntity address) {
    entityManager.merge(address);
    return address;
  }
}
