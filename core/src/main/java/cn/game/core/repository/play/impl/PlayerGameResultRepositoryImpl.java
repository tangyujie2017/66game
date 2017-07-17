package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.PlayerGameResult;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.PlayerGameResultRepositoryCustom;

public class PlayerGameResultRepositoryImpl extends HibernateRepositoryImpl<PlayerGameResult> implements PlayerGameResultRepositoryCustom{
	@PersistenceContext
	  private EntityManager entityManager;
}
