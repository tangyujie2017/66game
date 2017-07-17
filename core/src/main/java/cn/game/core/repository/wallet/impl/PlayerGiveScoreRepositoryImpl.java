package cn.game.core.repository.wallet.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.wallet.PlayerGiveScore;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.wallet.custom.PlayerGiveScoreRepositoryCustom;

public class PlayerGiveScoreRepositoryImpl extends HibernateRepositoryImpl<PlayerGiveScore> implements PlayerGiveScoreRepositoryCustom {
	@PersistenceContext
	  private EntityManager entityManager;
}
