package cn.game.core.repository.user;



import java.util.List;
import java.util.Optional;

import cn.game.core.repository.HibernateRepository;
import cn.game.core.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
public interface UserRepositoryCustom extends HibernateRepository<User>{

  public Optional<User> findByLogin(String login);

  public List<User> findActive();

}
