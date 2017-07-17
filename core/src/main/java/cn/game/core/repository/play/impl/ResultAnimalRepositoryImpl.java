package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.ResultAnimal;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.ResultAnimalRepositoryCustom;

public class ResultAnimalRepositoryImpl  extends HibernateRepositoryImpl<ResultAnimal> implements ResultAnimalRepositoryCustom{
	@PersistenceContext
	  private EntityManager entityManager;
}
