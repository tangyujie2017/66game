package cn.game.core.repository.system;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.system.Customer;
import cn.game.core.repository.system.custom.CustomerRepositoryCustom;
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>, CustomerRepositoryCustom {
    public Customer findByMobile(String mobile);
}
