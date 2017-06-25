package cn.game.core.repository.system.impl;

import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.system.CustomerRole;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.system.custom.CustomerRoleRepositoryCustom;

@Repository
public class CustomerRoleRepositoryCustomImpl extends HibernateRepositoryImpl<CustomerRole>
		implements CustomerRoleRepositoryCustom {

}
