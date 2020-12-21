package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {
  @PersistenceContext EntityManager entityManager;

  /**
   * Method takes a item uuid and returns the matching ItemEntity
   *
   * @param uuid item uuid
   * @return ItemEntity
   */
  public ItemEntity getItemById(String uuid) {
    try {
      return entityManager
          .createNamedQuery("ItemEntity.getItemById", ItemEntity.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }
}
