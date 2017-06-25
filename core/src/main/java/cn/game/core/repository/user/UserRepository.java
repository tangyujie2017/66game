package cn.game.core.repository.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.system.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, UserRepositoryCustom {
	public User findByMobile(String mobile);

}
