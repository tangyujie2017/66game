package cn.game.core.repository.authority;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.table.Authority;
import cn.game.core.table.User;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zzc on 11/11/2016.
 */

public class AuthorityRepositoryImpl extends HibernateRepositoryImpl<Authority> implements AuthorityRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

 


}