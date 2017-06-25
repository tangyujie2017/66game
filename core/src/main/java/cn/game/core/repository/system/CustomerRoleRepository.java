package cn.game.core.repository.system;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.system.CustomerRole;

@Repository
public interface CustomerRoleRepository extends CrudRepository<CustomerRole, Long> {
	CustomerRole findByName(String name);
	CustomerRole findByDetails(String details);
}
