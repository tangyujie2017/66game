package cn.game.core.repository.customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.table.Customer;
import cn.game.core.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>, CustomerRepositoryCustom {
    public Customer findByMobile(String mobile);
    
    
}
