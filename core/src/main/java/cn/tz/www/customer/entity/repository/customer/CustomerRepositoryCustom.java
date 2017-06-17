package cn.tz.www.customer.entity.repository.customer;



import java.util.List;
import java.util.Optional;

import cn.tz.www.customer.entity.repository.HibernateRepository;
import cn.tz.www.customer.entity.table.Customer;
import cn.tz.www.customer.entity.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
public interface CustomerRepositoryCustom extends HibernateRepository<Customer>{

  public Optional<Customer> findByLogin(String login);

  public List<Customer> findActive();

}
