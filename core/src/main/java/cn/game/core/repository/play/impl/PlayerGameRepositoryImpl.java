package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.PlayerGame;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.PlayerGameRepositoryCustom;

public class PlayerGameRepositoryImpl extends HibernateRepositoryImpl<PlayerGame>
		implements PlayerGameRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;
}
