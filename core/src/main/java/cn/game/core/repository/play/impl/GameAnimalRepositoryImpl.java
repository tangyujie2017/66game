package cn.game.core.repository.play.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.play.GameAnimal;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.play.custom.GameAnimalRepositoryCustom;

public class GameAnimalRepositoryImpl extends HibernateRepositoryImpl<GameAnimal> implements GameAnimalRepositoryCustom{
	  @PersistenceContext
	  private EntityManager entityManager;
}
