package cn.game.core.repository.news;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.table.News;



/**
 * Created by zzc on 11/11/2016.
 */

public class NewsRepositoryImpl extends HibernateRepositoryImpl<News> implements NewsRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

 

}