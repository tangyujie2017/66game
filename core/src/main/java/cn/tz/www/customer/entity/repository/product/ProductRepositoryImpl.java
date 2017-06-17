package cn.tz.www.customer.entity.repository.product;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.tz.www.customer.entity.repository.HibernateRepositoryImpl;
import cn.tz.www.customer.entity.table.Product;



/**
 * Created by zzc on 11/11/2016.
 */

public class ProductRepositoryImpl extends HibernateRepositoryImpl<Product> implements ProductRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

  


}