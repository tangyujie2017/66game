package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.Player;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.PlayerRepositoryCustom;

public class PlayerRepositoryImpl extends HibernateRepositoryImpl<Player> implements PlayerRepositoryCustom{
	@PersistenceContext
	  private EntityManager entityManager;
}
