package cn.tz.www.customer.entity.repository.role;

import org.springframework.stereotype.Repository;

import cn.tz.www.customer.entity.repository.HibernateRepositoryImpl;
import cn.tz.www.customer.entity.table.Role;



@Repository
public class RoleRepositoryCustomImpl extends HibernateRepositoryImpl<Role> implements RoleRepositoryCustom {

}
