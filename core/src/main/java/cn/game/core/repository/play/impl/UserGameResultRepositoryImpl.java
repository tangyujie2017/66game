package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.UserGameResult;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.UserGameResultRepositoryCustom;

public class UserGameResultRepositoryImpl extends HibernateRepositoryImpl<UserGameResult> implements UserGameResultRepositoryCustom{
	@PersistenceContext
	  private EntityManager entityManager;
}
