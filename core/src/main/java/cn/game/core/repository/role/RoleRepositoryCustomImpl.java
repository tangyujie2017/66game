package cn.game.core.repository.role;

import org.springframework.stereotype.Repository;

import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.table.Role;



@Repository
public class RoleRepositoryCustomImpl extends HibernateRepositoryImpl<Role> implements RoleRepositoryCustom {

}
