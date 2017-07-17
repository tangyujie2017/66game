package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.PlayerAgency;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.PlayerAgencyRepositoryCustom;

public class PlayerAgencyRepositoryImpl extends HibernateRepositoryImpl<PlayerAgency> implements PlayerAgencyRepositoryCustom{
	  @PersistenceContext
	  private EntityManager entityManager;
}
