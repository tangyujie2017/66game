package cn.game.core.repository.user;

import java.util.List;
import java.util.Optional;

import cn.game.core.entity.table.system.User;
import cn.game.core.repository.HibernateRepository;

public interface UserRepositoryCustom extends HibernateRepository<User> {

	public Optional<User> findByLogin(String login);

	public List<User> findActive();

}
