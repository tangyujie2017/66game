package cn.game.core.repository.customerAuthority;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.table.Authority;
import cn.game.core.table.CustomerAuthority;
import cn.game.core.table.User;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zzc on 11/11/2016.
 */

public class CustomerAuthorityRepositoryImpl extends HibernateRepositoryImpl<CustomerAuthority> implements CustomerAuthorityRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

 


}