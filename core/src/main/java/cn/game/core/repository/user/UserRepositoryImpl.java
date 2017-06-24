package cn.game.core.repository.user;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.table.User;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zzc on 11/11/2016.
 */

public class UserRepositoryImpl extends HibernateRepositoryImpl<User> implements UserRepositoryCustom {


  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<User> findByLogin(String login) {
    String hql = "select u from User u where locked = false and login = :login";
    TypedQuery<User> q = entityManager.createQuery(hql, User.class);
    q.setParameter("login", login);
    User user = null;
    try {
      user = q.getSingleResult();
    } catch (Exception e){
      // do nothing
    }
    return Optional.ofNullable(user);
  }

  @Override
  public List<User> findActive() {
    String hql = "select u from User u where locked = false";
    TypedQuery<User> q = entityManager.createQuery(hql, User.class);
    return q.getResultList();
  }


}