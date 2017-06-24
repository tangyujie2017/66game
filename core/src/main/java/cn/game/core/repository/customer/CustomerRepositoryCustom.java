package cn.game.core.repository.customer;



import java.util.List;
import java.util.Optional;

import cn.game.core.repository.HibernateRepository;
import cn.game.core.table.Customer;
import cn.game.core.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
public interface CustomerRepositoryCustom extends HibernateRepository<Customer>{

  public Optional<Customer> findByLogin(String login);

  public List<Customer> findActive();

}
