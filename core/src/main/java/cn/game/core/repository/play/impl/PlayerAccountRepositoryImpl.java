package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.PlayerAccount;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.PlayerAccountRepositoryCustom;

public class PlayerAccountRepositoryImpl extends HibernateRepositoryImpl<PlayerAccount> implements PlayerAccountRepositoryCustom{
	@PersistenceContext
	  private EntityManager entityManager;
}
