package cn.game.core.repository.system.custom;

import java.util.List;
import java.util.Optional;

import cn.game.core.entity.table.system.Customer;
import cn.game.core.repository.HibernateRepository;

public interface CustomerRepositoryCustom extends HibernateRepository<Customer> {

	public Optional<Customer> findByLogin(String login);

	public List<Customer> findActive();

}
