package cn.game.core.repository.system.impl;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.system.custom.AuthorityRepositoryCustom;
public class AuthorityRepositoryImpl extends HibernateRepositoryImpl<cn.game.core.entity.table.system.Authority> implements AuthorityRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

 


}