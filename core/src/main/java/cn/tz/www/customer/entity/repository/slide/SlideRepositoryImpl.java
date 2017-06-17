package cn.tz.www.customer.entity.repository.slide;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.tz.www.customer.entity.repository.HibernateRepositoryImpl;
import cn.tz.www.customer.entity.table.Slide;



/**
 * Created by zzc on 11/11/2016.
 */

public class SlideRepositoryImpl extends HibernateRepositoryImpl<Slide> implements SlideRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

  


}