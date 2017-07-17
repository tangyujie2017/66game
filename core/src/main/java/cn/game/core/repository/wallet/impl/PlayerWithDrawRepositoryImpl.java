package cn.game.core.repository.wallet.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.wallet.PlayerWithDraw;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.wallet.custom.PlayerWithDrawRepositoryCustom;

public class PlayerWithDrawRepositoryImpl extends HibernateRepositoryImpl<PlayerWithDraw> implements PlayerWithDrawRepositoryCustom{
	@PersistenceContext
	  private EntityManager entityManager;
}
