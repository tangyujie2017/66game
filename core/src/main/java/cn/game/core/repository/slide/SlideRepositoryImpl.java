package cn.game.core.repository.slide;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.table.Slide;



/**
 * Created by zzc on 11/11/2016.
 */

public class SlideRepositoryImpl extends HibernateRepositoryImpl<Slide> implements SlideRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

  


}