package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.BaseAnimal;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.BaseAnimalRepositoryCustom;

public class BaseAnimalRepositoryImpl extends HibernateRepositoryImpl<BaseAnimal> implements BaseAnimalRepositoryCustom{
	 @PersistenceContext
	  private EntityManager entityManager;
}
