package cn.tz.www.customer.entity.repository.customerRole;

import org.springframework.stereotype.Repository;

import cn.tz.www.customer.entity.repository.HibernateRepositoryImpl;
import cn.tz.www.customer.entity.table.CustomerRole;
import cn.tz.www.customer.entity.table.Role;



@Repository
public class CustomerRoleRepositoryCustomImpl extends HibernateRepositoryImpl<CustomerRole> implements CustomerRoleRepositoryCustom {

}
