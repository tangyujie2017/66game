package cn.game.core.repository.system.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cn.game.core.entity.table.system.CustomerAuthority;
import cn.game.core.repository.HibernateRepositoryImpl;
import cn.game.core.repository.system.custom.CustomerAuthorityRepositoryCustom;

public class CustomerAuthorityRepositoryImpl extends HibernateRepositoryImpl<CustomerAuthority>
		implements CustomerAuthorityRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

}