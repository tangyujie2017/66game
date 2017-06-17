package cn.tz.www.customer.entity.repository.user;



import java.util.List;
import java.util.Optional;

import cn.tz.www.customer.entity.repository.HibernateRepository;
import cn.tz.www.customer.entity.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
public interface UserRepositoryCustom extends HibernateRepository<User>{

  public Optional<User> findByLogin(String login);

  public List<User> findActive();

}
