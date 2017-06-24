package cn.game.core.repository.customerRole;

import org.springframework.stereotype.Repository;

import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.table.CustomerRole;
import cn.game.core.table.Role;



@Repository
public class CustomerRoleRepositoryCustomImpl extends HibernateRepositoryImpl<CustomerRole> implements CustomerRoleRepositoryCustom {

}
