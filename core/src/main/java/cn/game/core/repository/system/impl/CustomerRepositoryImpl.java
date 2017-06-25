package cn.game.core.repository.system.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import cn.game.core.entity.table.system.Customer;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.system.custom.CustomerRepositoryCustom;

import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl extends HibernateRepositoryImpl<Customer> implements CustomerRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<Customer> findByLogin(String login) {
		String hql = "select u from Customer u where locked = false and login = :login";
		TypedQuery<Customer> q = entityManager.createQuery(hql, Customer.class);
		q.setParameter("login", login);
		Customer user = null;
		try {
			user = q.getSingleResult();
		} catch (Exception e) {
			// do nothing
		}
		return Optional.ofNullable(user);
	}

	@Override
	public List<Customer> findActive() {
		String hql = "select u from Customer u where locked = false";
		TypedQuery<Customer> q = entityManager.createQuery(hql, Customer.class);
		return q.getResultList();
	}

}