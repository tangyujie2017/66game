package cn.tz.www.customer.entity.repository.news;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.tz.www.customer.entity.repository.HibernateRepositoryImpl;
import cn.tz.www.customer.entity.table.News;



/**
 * Created by zzc on 11/11/2016.
 */

public class NewsRepositoryImpl extends HibernateRepositoryImpl<News> implements NewsRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

 

}