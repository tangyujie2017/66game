package cn.tz.www.customer.entity.repository.customerRole;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.tz.www.customer.entity.table.CustomerRole;
import cn.tz.www.customer.entity.table.Role;


/**
 * Created by zzc on 16/11/2016.
 */
@Repository
public interface CustomerRoleRepository extends CrudRepository<CustomerRole, Long> {
	CustomerRole findByName(String name);
	CustomerRole findByDetails(String details);
}
