package cn.game.core.repository;

import java.util.List;
import java.util.Map;

import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

public interface HibernateRepository<T> {
	
	Page<T> findPageByGroups(Groups groups, Page<T> page, String sql, Class<?> classes);
	
	Page<T> findPage(Page<T> page, String hql, Map<String,Object> parameter);
	
	@SuppressWarnings("rawtypes")
	List findByGroups(Groups groups, String sql, Class<?> classes);
	
	Page<T> findEntityPageByGroups(Groups groups, Page<T> page);
	
	Page<T> findEntityPageByGroups(Groups groups, String hql, Page<T> page);
	
	List<T> findEntityByGroups(Groups groups, String hql);
	
	List<T> findEntityByGroups(Groups groups);
	
	List<Object> findBySql(Groups groups, String[] selectStr, String[] groupStr);
	
	List<Object> findBySql(String sql,Map<String,Object> parameter);
	
	List<Object> findByHql(Groups groups, String[] selectStr, String[] groupStr);
	
	Long findSumByGroupsAlias(Groups groups, String field);
	
	List<Long> findEntityByGroupsAlias(Groups groups, String field);
	
	List<Long> findSqlByGroupsAlias(Groups groups, String field);
	
	long findTotalCountByGroups(Groups groups);
	
	T find(Long id);
	
	T findUniqueBy(final String propertyName, final Object value);
	
	void persist(Object entity);
	
	void update(Object entity);
	
	void remove(Object entity);
	
	Integer excuteSql(String sql,Map<String,Object> parameter);
	Integer excuteIntgerSql(String sql,Map<Integer,Object> parameter);
	void logicDelete(Long id) throws Exception;
	
	void restoreEntity(Long id) throws Exception;

}
