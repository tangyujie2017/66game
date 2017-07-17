package cn.game.core.repository.wallet.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.wallet.PlayerRecharge;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.wallet.custom.PlayerRechargeRepositoryCustom;

public class PlayerRechargeRepositoryImpl extends HibernateRepositoryImpl<PlayerRecharge> implements PlayerRechargeRepositoryCustom{
	@PersistenceContext
	  private EntityManager entityManager;
}
