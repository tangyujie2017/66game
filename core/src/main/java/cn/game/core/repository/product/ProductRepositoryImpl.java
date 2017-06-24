package cn.game.core.repository.product;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.table.Product;



/**
 * Created by zzc on 11/11/2016.
 */

public class ProductRepositoryImpl extends HibernateRepositoryImpl<Product> implements ProductRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

  


}