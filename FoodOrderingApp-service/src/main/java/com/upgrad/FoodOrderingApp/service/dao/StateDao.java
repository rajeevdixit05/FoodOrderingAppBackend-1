package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {

  @PersistenceContext private EntityManager entityManager;

  @Transactional(propagation = Propagation.REQUIRED)
  public StateEntity findStateByUUID(final String uuid) {
    try {
      return entityManager
          .createNamedQuery("fetchStateByUUID", StateEntity.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public List<StateEntity> getAllStates() {
    try {
      List<StateEntity> states =
          entityManager.createNamedQuery("fetchAllStates", StateEntity.class).getResultList();
      return states;
    } catch (NoResultException nre) {
      return null;
    }
  }
}
