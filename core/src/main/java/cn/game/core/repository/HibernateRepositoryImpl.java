package cn.game.core.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import cn.game.core.entity.BaseEntity;
import cn.game.core.tools.CommonUtil;
import cn.game.core.tools.Group;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Order;
import cn.game.core.tools.Page;
import cn.game.core.tools.ReflectionUtils;
import cn.game.core.tools.SearchAnnotation;
import cn.game.core.tools.Order.SortType;
import cn.game.core.tools.PropertyFilter.MatchType;

@NoRepositoryBean
public class HibernateRepositoryImpl<T> implements HibernateRepository<T> {

	private EntityManager entityManger;

	private Class<T> entityClass;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Resource
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Resource
	public void setEntityManger(EntityManager entityManger) {
		this.entityManger = entityManger;
	}

	public HibernateRepositoryImpl() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	public Page<T> findPageByGroups(Groups groups, Page<T> page, String sql, Class<?> classes) {
		Integer currentPage = page.getCurrentPage();
		Integer pageSize = page.getPageSize();
		Map<String, Object> map = new HashMap<String, Object>();
		sql = createSqlByGroupsAll(sql, groups, map);

		Integer totalCount = countSqlResult(sql, map);

		String querySql = sql + " limit :offset ,:pageSize";
		int offset = (currentPage - 1) * pageSize; // 起始下标
		offset = offset > 0 ? offset : 0;
		offset = offset < totalCount ? offset : totalCount;
		map.put("offset", offset);
		map.put("pageSize", pageSize);// 分页用的

		List<?> items = namedParameterJdbcTemplate.query(querySql, map, BeanPropertyRowMapper.newInstance(classes));

		// List<?> items =
		// namedParameterJdbcTemplate.getJdbcOperations().query(querySql,
		// map.values().toArray(),
		// BeanPropertyRowMapper.newInstance(classes));
		page.setTotalCount(totalCount);
		page.setItems(items);

		return page;
	}

	@SuppressWarnings("rawtypes")
	public List findByGroups(Groups groups, String sql, Class<?> classes) {
		Map<String, Object> map = new HashMap<String, Object>();
		sql = createSqlByGroupsAll(sql, groups, map);
		List<?> items = namedParameterJdbcTemplate.query(sql, map, BeanPropertyRowMapper.newInstance(classes));
		return items;
	}

	public Page<T> findEntityPageByGroups(Groups groups, String hql, Page<T> page) {
		Map<String, Object> values = new HashMap<String, Object>();

		hql = createSqlByGroupsAll(hql, groups, values);// 将其按照sql的方式处理，因为hql提前有写好完整的语句

		page = findPage(page, hql, values);

		return page;
	}

	@SuppressWarnings("unchecked")
	public List<T> findEntityByGroups(Groups groups, String hql) {
		Map<String, Object> values = new HashMap<String, Object>();

		hql = createSqlByGroupsAll(hql, groups, values);// 将其按照sql的方式处理，因为hql提前有写好完整的语句

		return findListByHql(hql, values);
	}

	public Page<T> findEntityPageByGroups(Groups groups, Page<T> page) {

		Map<String, Object> values = new HashMap<String, Object>();

		String hql = createHqlByGroupsAll("", groups, values);

		page = findPage(page, hql, values);

		return page;
	}

	@SuppressWarnings("unchecked")
	public List<T> findEntityByGroups(Groups groups) {
		System.out.println("-------------------------------------");
		Map<String, Object> values = new HashMap<String, Object>();

		String hql = createHqlByGroupsAll("", groups, values);
		System.out.println(hql);
		return findListByHql(hql, values);
	}

	public Page<T> findPage(final Page<T> page, final String hql, final Map<String, Object> parameter) {
		TypedQuery<T> q = createTypedQuery(hql, parameter);
		Long totalCount = countResult(hql, parameter);
		Integer total = totalCount.intValue();
		page.setTotalCount(total);

		int pageNo = page.getCurrentPage();
		int pageSize = page.getPageSize();
		int first = (pageNo - 1) * pageSize;
		int totalPageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
		page.setTotalPageCount(totalPageCount);
		if (page.getFromIndex() > -1) {
			first = page.getFromIndex();
			pageNo = (first + 1) % pageSize == 0 ? (first + 1) / pageSize : (first + 1) / pageSize + 1;
			if (first + pageSize >= total - 1) {
				pageNo = totalPageCount;
			}
			page.setCurrentPage(pageNo);
		}
		q.setFirstResult(first);
		q.setMaxResults(pageSize);

		page.setItems(q.getResultList());
		return page;
	}

	/**
	 * 根据条件返回查询总行数
	 */
	public long findTotalCountByGroups(Groups groups) {

		Map<String, Object> values = new HashMap<String, Object>();

		String hql = createHqlByGroupsAll("", groups, values);

		long totalCount = countHqlResult(hql, values);

		return totalCount;
	}

	@Override
	public void persist(Object entity) {
		entityManger.persist(entity);
	}

	@Override
	public void update(Object entity) {
		entityManger.merge(entity);
	}

	@Override
	public void remove(Object entity) {
		entityManger.remove(entity);
	}

	public Integer excuteSql(String sql, Map<String, Object> parameter) {
		Query query = entityManger.createNativeQuery(sql);
		if (parameter != null) {
			for (Map.Entry<String, Object> entry : parameter.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.executeUpdate();
	}

	public Integer excuteIntgerSql(String sql, Map<Integer, Object> parameter) {
		Query query = entityManger.createNativeQuery(sql);
		if (parameter != null) {
			for (Map.Entry<Integer, Object> entry : parameter.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.executeUpdate();
	}

	/**
	 * 逻辑删除
	 * 
	 * @param id
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Override
	public void logicDelete(Long id) throws Exception {
		T entity = find(id);
		Class clazz = entity.getClass();
		Method method = clazz.getMethod("setEnable", Boolean.class);
		method.invoke(entity, false);
		update(entity);
	}

	/**
	 * 恢复逻辑删除的实体
	 */
	public void restoreEntity(Long id) throws Exception {
		T entity = find(id);
		Class clazz = entity.getClass();
		Method method = clazz.getMethod("setEnable", Boolean.class);
		method.invoke(entity, true);
		update(entity);
	}

	public T find(Long id) {
		return (T) entityManger.find(entityClass, id);
	}

	public T findUniqueBy(final String propertyName, final Object value) {
		try {
			String keyName = propertyName.replace(".", "");
			Class<T> tempclass = ReflectionUtils.getSuperClassGenricType(this.getClass());
			String hql = " from " + tempclass.getSimpleName() + " where " + propertyName + "=:" + keyName;
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put(keyName, value);
			TypedQuery<T> q = createTypedQuery(hql, parameter);
			List<T> list = q.getResultList();
			if (list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findBySql(Groups groups, String[] selectStr, String[] groupStr) {
		String sql = "";
		if (groups.getOrderby() != null) {
			groups.setOrderby("");
		}
		Map<String, Object> objects = new HashMap<String, Object>();
		sql = composeString2(groups, null, selectStr, groupStr, objects);
		Query q = createSqlQuery(sql, objects);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object> findBySql(String sql, Map<String, Object> parameter) {
		Query q = createSqlQuery(sql, parameter);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object> findByHql(Groups groups, String[] selectStr, String[] groupStr) {
		String hql = "";
		if (groups.getOrderby() != null) {
			groups.setOrderby("");
		}
		Map<String, Object> objects = new HashMap<String, Object>();
		hql = composeString(groups, null, selectStr, groupStr, objects);
		Query q = createQuery(hql, objects);

		return q.getResultList();
	}

	public Long findSumByGroupsAlias(Groups groups, String field) {
		List<Object> objs = findByHql(groups, new String[] { field }, null);
		Long result = 0L;
		for (Object obj : objs) {
			result += Long.valueOf(obj.toString());
		}
		return result;
	}

	public List<Long> findEntityByGroupsAlias(Groups groups, String field) {
		List<Long> userIds = new ArrayList<>();
		List<Object> objs = findByHql(groups, new String[] { field }, new String[] { field });
		for (Object obj : objs) {
			if (obj instanceof Object[]) {
				Object[] arrays = (Object[]) obj;
				if (arrays.length > 1) {
					userIds.add(Long.valueOf(arrays[1].toString()));
				}
			} else {
				userIds.add(Long.valueOf(obj.toString()));
			}
		}
		return userIds;
	}

	public List<Long> findSqlByGroupsAlias(Groups groups, String field) {
		List<Long> userIds = new ArrayList<>();
		List<Object> objs = findBySql(groups, new String[] { field }, new String[] { field });
		for (Object obj : objs) {
			userIds.add(Long.valueOf(obj.toString()));
		}
		return userIds;
	}

	@SuppressWarnings("rawtypes")
	protected List findListByHql(String hql, Map<String, Object> parameter) {
		Query q = createQuery(hql, parameter);
		return q.getResultList();
	}

	protected Integer countSqlResult(final String sql, final Map<String, Object> parameter) {
		String countSql = prepareCount(sql);
		try {
			BigInteger count = findUniqueBySql(countSql, parameter);

			return count.intValue();
		} catch (Exception e) {
			throw new RuntimeException("sql can't be auto count, sql is:" + countSql, e);
		}
	}

	protected long countHqlResult(final String hql, final Map<String, Object> parameter) {
		String countHql = prepareCountHql(hql);
		try {
			Long count = findUnique(countHql, parameter);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	protected String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		// select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(id) " + fromHql;
		return countHql;
	}

	@SuppressWarnings("unchecked")
	protected <X> X findUnique(final String hql, final Map<String, Object> parameter) {
		return (X) createQuery(hql, parameter).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	protected <X> X findUniqueBySql(final String sql, final Map<String, Object> parameter) {
		return (X) createSqlQuery(sql, parameter).getSingleResult();
	}

	protected Long countResult(final String hql, final Map<String, Object> parameter) {
		String countHql = prepareCount(hql);
		try {
			Long count = findUnique(countHql, parameter);

			return count;
		} catch (Exception e) {
			throw new RuntimeException("sql can't be auto count, sql is:" + countHql, e);
		}
	}

	private Query createQuery(final String hql, final Map<String, Object> parameter) {
		Query query = entityManger.createQuery(hql);
		if (parameter != null) {
			for (Map.Entry<String, Object> entry : parameter.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query;
	}

	private TypedQuery<T> createTypedQuery(final String hql, final Map<String, Object> parameter) {
		TypedQuery<T> query = entityManger.createQuery(hql, entityClass);
		if (parameter != null) {
			for (Map.Entry<String, Object> entry : parameter.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query;
	}

	private String createHqlByGroupsAll(String hql, Groups groups, Map<String, Object> values) {
		// from段
		StringBuffer fromBuffer = new StringBuffer(" ");
		// where 段
		StringBuffer whereBufferQian = new StringBuffer(" where 1=1 ");
		StringBuffer whereBufferHou = new StringBuffer("");

		// 存取相同前缀
		List<String> Alias1 = new LinkedList<String>();
		Class<T> tempclass = ReflectionUtils.getSuperClassGenricType(this.getClass());
		fromBuffer.append(" from " + tempclass.getSimpleName() + " as " + tempclass.getSimpleName());
		try {
			appendGroups(groups, fromBuffer, whereBufferQian, tempclass, Alias1, whereBufferHou, values);
		} catch (Exception e) {
			e.printStackTrace();
		}

		hql = fromBuffer.toString() + whereBufferQian.toString() + whereBufferHou.toString();

		String temp = "";
		if (groups.getOrderbys() != null && groups.getOrderbys().length > 0) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < groups.getOrderbys().length; i++) {// groups接受orderby数组进行多条件排序
				if (i == 0) {
					sBuffer.append(" order by ");
				}
				// order by 别名处理
				temp = groups.getOrderbys()[i];

				if (temp.isEmpty())
					continue;

				if (groups.getOrders()[i]) {
					sBuffer.append(tempclass.getSimpleName() + "." + temp + " asc,");
				} else {
					sBuffer.append(tempclass.getSimpleName() + "." + temp + " desc,");
				}
			}
			hql += sBuffer.deleteCharAt(sBuffer.length() - 1).toString();
		} else if (null != groups.getOrderby() && !groups.getOrderby().trim().equals("")) {
			// 处理order by的别名
			temp = groups.getOrderby();

			if (groups.isOrder()) {
				hql += " order by " + tempclass.getSimpleName() + "." + temp + " asc";
			} else {
				hql += " order by " + tempclass.getSimpleName() + "." + temp + " desc";
			}
		}

		return hql;
	}

	/**
	 * 只支持HQL
	 * 
	 * @param groups
	 * @param page
	 * @param selectStr
	 * @param groupStr
	 * @param values
	 * @return
	 */
	private String composeString(final Groups groups, Page<T> page, String[] selectStr, String[] groupStr,
			Map<String, Object> values) {
		// from段
		StringBuffer fromBuffer = new StringBuffer(" ");
		// where 段
		StringBuffer whereBufferQian = new StringBuffer(" where 1=1 ");
		StringBuffer whereBufferHou = new StringBuffer("");

		// 存取相同前缀
		List<String> Alias1 = new LinkedList<String>();
		Class<T> tempclass = ReflectionUtils.getSuperClassGenricType(this.getClass());
		fromBuffer.append(" from " + tempclass.getSimpleName() + " as " + tempclass.getSimpleName());

		// 有条件组内容
		appendGroups(groups, fromBuffer, whereBufferQian, tempclass, Alias1, whereBufferHou, values);
		String hql = "";
		List<Order> orders = new ArrayList<Order>();
		if ((groups.getOrderbys() == null || groups.getOrderbys().length <= 0) && groups.getOrderby() != null
				&& !groups.getOrderby().equals("")) {
			Order order = new Order();
			order.setField(groups.getOrderby());
			if (groups.isOrder()) {
				order.setSortType(SortType.ASC);
			} else {
				order.setSortType(SortType.DESC);
			}
			orders.add(order);
		}
		if (groups.getOrderbys() != null && groups.getOrderbys().length > 0) {
			for (int i = 0; i < groups.getOrderbys().length; i++) {
				Order order = new Order();
				order.setField(groups.getOrderbys()[i]);
				if (groups.getOrders()[i]) {
					order.setSortType(SortType.ASC);
				} else {
					order.setSortType(SortType.DESC);
				}
				orders.add(order);
			}
		}

		StringBuffer orderBuffer = new StringBuffer();
		if (orders.size() > 0) {
			orderBuffer = new StringBuffer(" order by ");
			appendOrder(orders, orderBuffer, tempclass, Alias1, fromBuffer, whereBufferQian);

		}
		StringBuffer slectBuffer = new StringBuffer(" select ");
		if (selectStr != null && selectStr.length != 0) {
			appendSelect(selectStr, slectBuffer, tempclass, Alias1, fromBuffer, whereBufferQian);
			if (!slectBuffer.toString().trim().equals("select")) {
				hql += slectBuffer.toString() + " ";
			}
		}
		if (whereBufferQian.toString().trim().equals("where 1=1")) {
			hql += fromBuffer.append(whereBufferQian).append("  ").append(whereBufferHou).append(" ").toString();
		} else {
			hql += fromBuffer.append(whereBufferQian).append(" and ").append(whereBufferHou).append(" ").toString();
		}
		if (groupStr != null && groupStr.length != 0) {
			StringBuffer groupBuffer = new StringBuffer(" group by  ");
			appendGroup(groupStr, groupBuffer, tempclass, Alias1, fromBuffer, whereBufferQian);
			if (!groupBuffer.toString().trim().equals("groupby")) {
				hql += groupBuffer.toString() + " ";
			}
		}
		if (orderBuffer.toString().length() != 0) {
			hql += orderBuffer;
		}
		return hql;
	}

	/**
	 * 只支持SQL
	 * 
	 * @param groups
	 * @param page
	 * @param selectStr
	 * @param groupStr
	 * @param values
	 * @return
	 */
	private String composeString2(final Groups groups, Page<T> page, String[] selectStr, String[] groupStr,
			Map<String, Object> values) {
		// from段
		StringBuffer fromBuffer = new StringBuffer(" ");
		// where 段
		StringBuffer whereBufferQian = new StringBuffer(" where 1=1 ");
		StringBuffer whereBufferHou = new StringBuffer("");

		// 存取相同前缀
		List<String> Alias1 = new LinkedList<String>();
		Class<T> tempclass = ReflectionUtils.getSuperClassGenricType(this.getClass());
		String className = tempclass.getSimpleName();
		String table = CommonUtil.camelToUnderline(className);
		fromBuffer.append(" from " + table + " as " + className);

		// 有条件组内容
		appendGroups2(groups, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
		// appendGroups(groups, fromBuffer, whereBufferQian, tempclass, Alias1,
		// whereBufferHou, values);
		String sql = "";
		List<Order> orders = new ArrayList<Order>();
		if ((groups.getOrderbys() == null || groups.getOrderbys().length <= 0) && groups.getOrderby() != null
				&& !groups.getOrderby().equals("")) {
			Order order = new Order();
			order.setField(groups.getOrderby());
			if (groups.isOrder()) {
				order.setSortType(SortType.ASC);
			} else {
				order.setSortType(SortType.DESC);
			}
			orders.add(order);
		}
		if (groups.getOrderbys() != null && groups.getOrderbys().length > 0) {
			for (int i = 0; i < groups.getOrderbys().length; i++) {
				Order order = new Order();
				order.setField(groups.getOrderbys()[i]);
				if (groups.getOrders()[i]) {
					order.setSortType(SortType.ASC);
				} else {
					order.setSortType(SortType.DESC);
				}
				orders.add(order);
			}
		}

		StringBuffer orderBuffer = new StringBuffer();
		if (orders.size() > 0) {
			orderBuffer = new StringBuffer(" order by ");
			appendOrder2(orders, orderBuffer, tempclass, Alias1, fromBuffer, whereBufferQian);

		}
		StringBuffer slectBuffer = new StringBuffer(" select ");
		if (selectStr != null && selectStr.length != 0) {
			appendSelect2(selectStr, slectBuffer, tempclass, Alias1, fromBuffer, whereBufferQian);
			if (!slectBuffer.toString().trim().equals("select")) {
				sql += slectBuffer.toString() + " ";
			}
		}
		if (whereBufferQian.toString().trim().equals("where 1=1")) {
			sql += fromBuffer.append(whereBufferQian).append("  ").append(whereBufferHou).append(" ").toString();
		} else {
			sql += fromBuffer.append(whereBufferQian).append(" and ").append(whereBufferHou).append(" ").toString();
		}
		if (groupStr != null && groupStr.length != 0) {
			StringBuffer groupBuffer = new StringBuffer(" group by  ");
			appendGroup2(groupStr, groupBuffer, tempclass, Alias1, fromBuffer, whereBufferQian);
			if (!groupBuffer.toString().trim().equals("groupby")) {
				sql += groupBuffer.toString() + " ";
			}
		}
		if (orderBuffer.toString().length() != 0) {
			sql += orderBuffer;
		}
		return sql;
	}

	private void appendSelect(String[] selectStrings, StringBuffer slectBuffer, Class<?> baseClass, List<String> Alias1,
			StringBuffer fromBuffer, StringBuffer whereBufferQian) {
		if (selectStrings == null || selectStrings.length == 0) {

		} else {
			for (String selectStr : selectStrings) {
				String tem = "";
				if (selectStr.contains("sum")) {
					tem = selectStr.substring(selectStr.indexOf("(") + 1, selectStr.indexOf(")"));
				} else if (selectStr.contains("count")) {
					tem = selectStr.substring(selectStr.indexOf("(") + 1, selectStr.indexOf(")"));
				} else {
					tem = selectStr;
				}
				String[] strings = CommonUtil.split(tem, ".");
				if (strings == null || strings.length == 0) {
					System.out.println("查询参数错误，请查证！");
				}
				Class<?> temClass = baseClass;
				Field value = null;
				StringBuffer temBuffer = new StringBuffer();
				String temName = baseClass.getSimpleName();
				for (String string : strings) {

					value = ReflectionUtils.getAllField(temClass, string);

					try {
						temClass = value.getType();
					} catch (Exception e) {
						slectBuffer.append(" '");
						slectBuffer.append(string);
						slectBuffer.append(" ' ");
						slectBuffer.append(",");
						continue;
					}
					temBuffer.append(string + ".");
					// 如果是集合
					if (ReflectionUtils.isInherit(temClass, List.class, true)) {
						SearchAnnotation searchAnnotation = value.getAnnotation(SearchAnnotation.class);
						if (searchAnnotation != null) {

							if (!Alias1.contains(temBuffer.subSequence(0, temBuffer.length() - 1))) {
								Alias1.add(temBuffer.subSequence(0, temBuffer.length() - 1).toString());
								fromBuffer.append(" left join ").append(temName).append(".").append(string).append(" ")
										.append(" as ").append(string);

							}
							temName = string;
							temClass = searchAnnotation.Class();
						} else {
							System.out.println("多对多关系必须要配置好注解searchAnnotation的别名");
						}
					}
					if (selectStr.contains("sum")) {
						slectBuffer.append(" sum( ");
					} else if (selectStr.contains("count")) {
						slectBuffer.append(" count( ");
					}
					slectBuffer.append(temName).append(".").append(string);
					if (selectStr.contains("sum") || selectStr.contains("count")) {
						slectBuffer.append(" ) ");
					}
					slectBuffer.append(",");
				}
			}
			slectBuffer = slectBuffer.delete(slectBuffer.length() - 1, slectBuffer.length());
		}
	}

	private void appendSelect2(String[] selectStrings, StringBuffer slectBuffer, Class<?> baseClass,
			List<String> Alias1, StringBuffer fromBuffer, StringBuffer whereBufferQian) {
		if (selectStrings == null || selectStrings.length == 0) {

		} else {
			for (String selectStr : selectStrings) {
				String tem = "";
				if (selectStr.contains("sum")) {
					tem = selectStr.substring(selectStr.indexOf("(") + 1, selectStr.indexOf(")"));
				} else if (selectStr.contains("count")) {
					tem = selectStr.substring(selectStr.indexOf("(") + 1, selectStr.indexOf(")"));
				} else {
					tem = selectStr;
				}
				if (selectStr.contains("sum")) {
					slectBuffer.append(" sum( ");
				} else if (selectStr.contains("count")) {
					slectBuffer.append(" count( ");
				}
				slectBuffer.append(tem);
				if (selectStr.contains("sum") || selectStr.contains("count")) {
					slectBuffer.append(" ) ");
				}
				slectBuffer.append(",");
			}
			slectBuffer = slectBuffer.delete(slectBuffer.length() - 1, slectBuffer.length());
		}
	}

	private void appendOrder(List<Order> orders, StringBuffer orderBuffer, Class<?> baseClass, List<String> Alias1,
			StringBuffer fromBuffer, StringBuffer whereBufferQian) {

		for (Order order : orders) {
			String tem = "";
			tem = order.getField();
			String[] strings = CommonUtil.split(tem, ".");
			if (strings == null || strings.length == 0) {
				System.out.println("查询参数错误，请查证！");
			}
			Class<?> temClass = baseClass;
			Field value = null;
			StringBuffer temBuffer = new StringBuffer();
			String temName = baseClass.getSimpleName();
			for (String string : strings) {
				value = ReflectionUtils.getAllField(temClass, string);
				temClass = value.getType();
				temBuffer.append(string + ".");
				// 如果是集合
				try {
					if (ReflectionUtils.isInherit(temClass, List.class, true)) {
						SearchAnnotation searchAnnotation = value.getAnnotation(SearchAnnotation.class);
						if (searchAnnotation != null) {
							if (!Alias1.contains(temBuffer.subSequence(0, temBuffer.length() - 1))) {
								Alias1.add(temBuffer.subSequence(0, temBuffer.length() - 1).toString());
								fromBuffer.append(" left join ").append(temName).append(".").append(string).append(" ")
										.append(" as ").append(string);
							}
							temClass = searchAnnotation.Class();
							temName = string;
						} else {
							System.out.println("多对多关系必须要配置好注解searchAnnotation的别名");
						}
					}
					orderBuffer.append(temName).append(".").append(string);
					if (order.getSortType() == SortType.ASC) {
						orderBuffer.append(" asc ");
					} else {
						orderBuffer.append(" desc ");
					}
					orderBuffer.append(",");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		orderBuffer = orderBuffer.delete(orderBuffer.length() - 1, orderBuffer.length());
	}

	private void appendOrder2(List<Order> orders, StringBuffer orderBuffer, Class<?> baseClass, List<String> Alias1,
			StringBuffer fromBuffer, StringBuffer whereBufferQian) {

		for (Order order : orders) {
			String tem = "";
			tem = order.getField();
			orderBuffer.append(tem);
			if (order.getSortType() == SortType.ASC) {
				orderBuffer.append(" asc ");
			} else {
				orderBuffer.append(" desc ");
			}
			orderBuffer.append(",");
		}
		orderBuffer = orderBuffer.delete(orderBuffer.length() - 1, orderBuffer.length());
	}

	private void appendGroup(String[] groupStrings, StringBuffer groupBuffer, Class<?> baseClass, List<String> Alias1,
			StringBuffer fromBuffer, StringBuffer whereBufferQian) {
		if (groupStrings == null || groupStrings.length == 0) {

		} else {
			for (String groupStr : groupStrings) {
				String tem = "";
				tem = groupStr;
				if (tem.contains("sum") || tem.contains("count")) {
					continue;
				}
				String[] strings = CommonUtil.split(tem, ".");

				if (strings == null || strings.length == 0) {
					System.out.println("查询参数错误，请查证！");
				}
				Class<?> temClass = baseClass;
				Field value = null;
				StringBuffer temBuffer = new StringBuffer();
				String temName = baseClass.getSimpleName();
				for (String string : strings) {
					value = ReflectionUtils.getAllField(temClass, string);
					temClass = value.getType();
					temBuffer.append(string + ".");
					// 如果是集合
					if (ReflectionUtils.isInherit(temClass, List.class, true)) {
						SearchAnnotation searchAnnotation = value.getAnnotation(SearchAnnotation.class);
						if (searchAnnotation != null) {

							if (!Alias1.contains(temBuffer.subSequence(0, temBuffer.length() - 1))) {
								Alias1.add(temBuffer.subSequence(0, temBuffer.length() - 1).toString());
								fromBuffer.append(" left join ").append(temName).append(".").append(string).append(" ")
										.append(" as ").append(string);

							}
							temClass = searchAnnotation.Class();
							temName = string;
						} else {
							System.out.println("多对多关系必须要配置好注解searchAnnotation的别名");
						}
					} else {
						groupBuffer.append(temName).append(".").append(string);
						groupBuffer.append(",");
					}
				}

			}
			groupBuffer = groupBuffer.delete(groupBuffer.length() - 1, groupBuffer.length());

		}
	}

	private void appendGroup2(String[] groupStrings, StringBuffer groupBuffer, Class<?> baseClass, List<String> Alias1,
			StringBuffer fromBuffer, StringBuffer whereBufferQian) {

		if (groupStrings == null || groupStrings.length == 0) {

		} else {
			for (String groupStr : groupStrings) {
				String tem = "";
				tem = groupStr;
				if (tem.contains("sum") || tem.contains("count")) {
					continue;
				}
				groupBuffer.append(groupStr);
				groupBuffer.append(",");

			}
			groupBuffer = groupBuffer.delete(groupBuffer.length() - 1, groupBuffer.length());
		}
	}

	private Query createSqlQuery(final String sql, final Map<String, Object> parameter) {

		Query query = entityManger.createNativeQuery(sql);
		if (parameter != null) {
			for (Map.Entry<String, Object> entry : parameter.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query;
	}

	private String prepareCount(String query) {
		// select子句与order by子句会影响count查询,进行简单的排除.
		query = "from " + StringUtils.substringAfterLast(query, "from");
		query = StringUtils.substringBefore(query, "order by");

		String count = "select count(*) " + query;
		return count;
	}

	private String createSqlByGroupsAll(String sql, Groups groups, Map<String, Object> values) {
		// from段
		StringBuffer fromBuffer = new StringBuffer(" ");
		// where 段
		StringBuffer whereBufferQian = new StringBuffer(" where 1=1 ");
		StringBuffer whereBufferHou = new StringBuffer("");

		// 存取相同前缀
		List<String> Alias1 = new LinkedList<String>();
		fromBuffer.append(sql);

		try {
			appendGroups2(groups, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sql = fromBuffer.toString() + whereBufferQian.toString() + whereBufferHou.toString();

		if (groups.getGroupby() != null && !"".equals(groups.getGroupby())) {
			sql += " group by " + groups.getGroupby();
		}

		String temp = "";
		if (groups.getOrderbys() != null && groups.getOrderbys().length > 0) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < groups.getOrderbys().length; i++) {// groups接受orderby数组进行多条件排序
				if (i == 0) {
					sBuffer.append(" order by ");
				}
				// order by 别名处理
				temp = groups.getOrderbys()[i];

				if (temp.isEmpty())
					continue;

				if (groups.getOrders()[i]) {
					sBuffer.append(temp + " asc,");
				} else {
					sBuffer.append(temp + " desc,");
				}
			}
			sql += sBuffer.deleteCharAt(sBuffer.length() - 1).toString();
		} else if (null != groups.getOrderby() && !"".equals(groups.getOrderby().trim())) {
			// 处理order by的别名
			temp = groups.getOrderby();

			if (groups.isOrder()) {
				sql += " order by " + temp + " asc";
			} else {
				sql += " order by " + temp + " desc";
			}
		}

		return sql;
	}

	private void appendGroups2(Groups groups, StringBuffer fromBuffer, StringBuffer whereBufferQian,
			List<String> Alias1, StringBuffer whereBufferHou, Map<String, Object> values) {
		if (groups.getGroupList() == null) {
			System.out.println("groups的GroupList不能为空！");
		} else {
			if (groups.getChildGroups2() != null && !groups.getChildGroups2().isEmpty()
					|| groups.getChildrelation() != null) {
				for (Group group : groups.getGroupList()) {
					appendGroup2(group, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
				}
				for (Groups tGroup : groups.getChildGroups2()) {
					if (tGroup.getChildrelation() == MatchType.AND) {
						whereBufferHou.append(" and ( ");
						appendGroups2(tGroup, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
						whereBufferHou.append(" )");
					} else if (tGroup.getChildrelation() == MatchType.OR) {
						whereBufferHou.append(" or ( ");
						appendGroups2(tGroup, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
						whereBufferHou.append(" )");
					}
				}
			} else {
				StringBuffer whereAnd = new StringBuffer();
				StringBuffer whereOr = new StringBuffer();
				// List<Object> tempList = new ArrayList<Object>();
				for (Group group : groups.getGroupList()) {
					if (group.getRelation().equals(MatchType.AND)) {
						appendGroup2(group, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
					} else {
						if (whereOr.toString().equals("")) {
							whereOr.append(" and ( ");
						}
						// tempList.add(group.getPropertyValue1());
						// values.remove(group.getPropertyValue1());
					}

				}
				if (!whereOr.toString().equals("")) {
					whereOr.append(" ) ");
				}
				// for (Object temp : tempList) {
				// values.add(temp);
				// }
				whereBufferHou.append(whereAnd).append(whereOr);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void appendGroup2(Group group, StringBuffer fromBuffer, StringBuffer whereBufferQian, List<String> Alias1,
			StringBuffer whereBufferHou, Map<String, Object> values) {

		String propertyName = group.getPropertyName();

		boolean isOver = buildCase2(group, whereBufferHou, propertyName);

		// 没有到最后（可能是 NULL/ NOT NULL）
		if (!isOver) {
			String matchCase = "";
			if (whereBufferHou.toString().trim().length() > 0) {
				matchCase = whereBufferHou.toString().trim().substring(whereBufferHou.toString().trim().length() - 1,
						whereBufferHou.toString().trim().length());
			}
			if (group.getMatchType() == MatchType.NULL || group.getMatchType() == MatchType.NOTNULL) {
				whereBufferHou.append(" ");
				if (group.getRelation() == MatchType.AND) {
					try {
						if (!matchCase.equals("(")) {
							whereBufferHou.append(" and ");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						if (!matchCase.equals("(")) {
							whereBufferHou.append(" or ");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				switch (group.getMatchType()) {
				case NOTIN:
					whereBufferHou.append(propertyName).append(" not in  ");
					break;
				case NOTNULL:
					whereBufferHou.append(propertyName).append(" is not null ");
					break;
				case NULL:
					whereBufferHou.append(propertyName).append(" is null ");
					break;
				}
			} else {
				System.out.println("拼接处的name不能以对象结尾，统一要以基本类型结尾！");
			}
		}

		addValues(group, whereBufferHou, values);
	}

	private void addValues(Group group, StringBuffer whereBufferHou, Map<String, Object> values) {
		String propertyName = group.getPropertyName();
		if (!CommonUtil.isNull(propertyName)) {
			propertyName = propertyName.replace(".", "");
			// 判断错误
			if (group.getPropertyValue1() == null
					&& !(group.getMatchType() == MatchType.NULL || group.getMatchType() == MatchType.NOTNULL)) {
				System.out.println("传入值为空，但并不是查询NULL OR NOT NULL 请查证！");
			} else if ("".equals(group.getPropertyValue1()) && group.getMatchType() == MatchType.NE) {
				whereBufferHou.append(" :" + propertyName + " ");
				values.put(propertyName, group.getPropertyValue1());
			} else if (group.getPropertyValue1() != null && !"".equals(group.getPropertyValue1())) {
				// 是in 或not in
				if (group.getMatchType() == MatchType.IN || group.getMatchType() == MatchType.NOTIN) {
					Collection<?> collection = (Collection<?>) group.getPropertyValue1();
					StringBuffer inBuffer = new StringBuffer(" ( ");
					inBuffer.append((":" + propertyName));
					List<Object> list = new ArrayList<Object>();
					for (Object object : collection) {
						list.add(object);
					}
					values.put(propertyName, list);
					inBuffer.append(" ) ");
					whereBufferHou.append(inBuffer);
				} else if (group.getMatchType() == MatchType.BETWEEN) {
					// 如果是bwt
					if (group.getPropertyValue2() == null) {
						System.out.println("第二个参数不能为空");
					}
					if (group.getPropertyValue1().getClass() == Date.class || group.isDate()
							|| group.getPropertyValue1().getClass() == java.sql.Timestamp.class) {
						values.put(propertyName, group.getPropertyValue1());
						values.put(propertyName + "2", group.getPropertyValue2());
						whereBufferHou.append(" :" + propertyName + " and :" + propertyName + "2 ");
					} else {
						System.out.println("BETWEEN 规定只能用于时间，数字请用大于小于进行");
					}

				} else {
					if (group.getPropertyValue1().getClass() == Date.class) {
						values.put(propertyName, group.getPropertyValue1());
						whereBufferHou.append(" :" + propertyName + " ");
					} else if (group.isDate()) {
						values.put(propertyName, group.getPropertyValue1());
						whereBufferHou.append(" :" + propertyName + " ");
					} else if (group.getPropertyValue1().getClass() == java.sql.Timestamp.class) {
						whereBufferHou.append(" :" + propertyName + " ");
					} else {
						whereBufferHou.append(" :" + propertyName + " ");
						if (group.getMatchType() == MatchType.LIKE) {
							values.put(propertyName, "%" + group.getPropertyValue1() + "%");
						} else {
							values.put(propertyName, group.getPropertyValue1());
						}
					}
				}
			} else if (group.getPropertyValue2() != null && !"".equals(group.getPropertyValue2())) {
				whereBufferHou.append(" :" + propertyName + " ");
				if (group.getMatchType() == MatchType.LIKE) {
					values.put(propertyName, "%" + group.getPropertyValue2() + "%");
				} else {
					values.put(propertyName, group.getPropertyValue2());
				}
			}
		}

	}

	private boolean buildCase2(Group group, StringBuffer whereBufferHou, String alisStr) {
		boolean isOver;
		isOver = true;
		whereBufferHou.append(" ");
		String matchCase = "";
		if (whereBufferHou.toString().trim().length() > 0) {
			matchCase = whereBufferHou.toString().trim().substring(whereBufferHou.toString().trim().length() - 1,
					whereBufferHou.toString().trim().length());
		}
		if (group.getRelation() == MatchType.AND) {
			if (!matchCase.equals("(")) {
				whereBufferHou.append(" and ");
			}
		} else {
			if (!matchCase.equals("(")) {
				whereBufferHou.append(" or ");
			}
		}

		matchCase(group, whereBufferHou, alisStr);

		return isOver;
	}

	/**
	 * @param group
	 * @param whereBufferHou
	 * @param temName
	 *            别名
	 * @param alisStr
	 */
	@SuppressWarnings("incomplete-switch")
	private void matchCase(Group group, StringBuffer whereBufferHou, String alisStr) {
		switch (group.getMatchType()) {
		case EQ:
			whereBufferHou.append(alisStr).append(" = ");
			break;
		case LIKE:
			whereBufferHou.append(alisStr).append(" like ");
			break;
		case LT:
			whereBufferHou.append(alisStr).append(" < ");
			break;
		case LE:
			if (group.getRelation() == MatchType.AND)
				whereBufferHou.append(alisStr).append(" <= ");
			break;
		case GT:
			whereBufferHou.append(alisStr).append(" > ");

			break;
		case GE:
			whereBufferHou.append(alisStr).append(" >= ");

			break;
		case NE:
			whereBufferHou.append(alisStr).append(" <> ");
			break;

		case IN:
			whereBufferHou.append(alisStr).append(" in ");
			break;
		case NOTIN:
			whereBufferHou.append(alisStr).append(" not in  ");

			break;
		case BETWEEN:
			whereBufferHou.append(alisStr).append(" between  ");

			break;
		case NULL:
			whereBufferHou.append(alisStr).append(" is null ");
			break;
		case NOTNULL:
			whereBufferHou.append(alisStr).append(" is not null ");
			break;
		}
	}

	private void appendGroups(Groups groups, StringBuffer fromBuffer, StringBuffer whereBufferQian, Class<?> tempclass,
			List<String> Alias1, StringBuffer whereBufferHou, Map<String, Object> values) {
		if (groups.getGroupList() == null) {
			System.out.println("groups的GroupList不能为空！");
		} else {
			if (groups.getChildGroups2() != null && groups.getChildGroups2().size() > 0
					|| groups.getChildrelation() != null) {
				for (Group group : groups.getGroupList()) {
					appendGroup(group, fromBuffer, whereBufferQian, tempclass, Alias1, whereBufferHou, values);
				}
				for (Groups tGroup : groups.getChildGroups2()) {
					if (tGroup.getChildrelation() == MatchType.AND) {
						whereBufferHou.append(" and ( ");
						appendGroups(tGroup, fromBuffer, whereBufferQian, tempclass, Alias1, whereBufferHou, values);
						whereBufferHou.append(" )");
					} else if (tGroup.getChildrelation() == MatchType.OR) {
						whereBufferHou.append(" or ( ");
						appendGroups(tGroup, fromBuffer, whereBufferQian, tempclass, Alias1, whereBufferHou, values);
						whereBufferHou.append(" )");
					}
				}
			} else {
				StringBuffer whereAnd = new StringBuffer();
				StringBuffer whereOr = new StringBuffer();
				// List<Object> tempList = new ArrayList<Object>();
				for (Group group : groups.getGroupList()) {
					if (group.getRelation().equals(MatchType.AND)) {
						appendGroup(group, fromBuffer, whereBufferQian, tempclass, Alias1, whereAnd, values);
					} else {
						if (whereOr.toString().equals("")) {
							whereOr.append(" and ( ");
						}
						appendGroup(group, fromBuffer, whereBufferQian, tempclass, Alias1, whereOr, values);
						// tempList.add(group.getPropertyValue1());
						// values.remove(group.getPropertyValue1());
					}

				}
				if (!whereOr.toString().equals("")) {
					whereOr.append(" ) ");
				}
				// for (Object temp : tempList) {
				// values.add(temp);
				// }
				whereBufferHou.append(whereAnd).append(whereOr);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void appendGroup(Group group, StringBuffer fromBuffer, StringBuffer whereBufferQian, Class<?> baseClass,
			List<String> Alias1, StringBuffer whereBufferHou, Map<String, Object> values) {

		String[] strings = CommonUtil.split(group.getPropertyName(), ".");
		if (strings == null || strings.length == 0) {
			System.out.println("查询参数错误，请查证！");
		}
		Class<?> temClass = baseClass;
		Field value = null;
		// 存放临时字符串
		StringBuffer temBuffer = new StringBuffer();
		// 得到基类的别名
		String temName = baseClass.getSimpleName();
		// 循环（）
		boolean isOver = false;
		List<String> tempStrs = new ArrayList<String>();
		int t = 0;
		for (String string : strings) {
			boolean isSame = false;
			// 有一样的名字
			if (tempStrs.contains(string)) {
				t++;
				isSame = true;
			}

			tempStrs.add(string);

			// 反射得到字段
			value = ReflectionUtils.getAllField(temClass, string);
			// 赋值到基类
			temClass = value.getType();
			// 加上全名
			String alisStr = "";
			if (!isSame)
				alisStr = string;
			else {
				alisStr = string + t;

			}
			temBuffer.append(alisStr + ".");
			// 如果是集合
			if (ReflectionUtils.isInherit(temClass, List.class, true)) {
				SearchAnnotation searchAnnotation = value.getAnnotation(SearchAnnotation.class);
				if (searchAnnotation != null) {// 查看别名是否存在

					if (!Alias1.contains(temBuffer.subSequence(0, temBuffer.length() - 1))) {
						Alias1.add(temBuffer.subSequence(0, temBuffer.length() - 1).toString());
						fromBuffer.append(" left join ").append(temName).append(".").append(alisStr).append(" ")
								.append(" as ").append(alisStr);

					}
					temClass = searchAnnotation.Class();
					temName = alisStr;
				} else {
					System.out.println("多对多关系必须要配置好注解searchAnnotation的别名");
				}
			} else if (ReflectionUtils.isInherit(temClass, BaseEntity.class, false)) {// 如果是类
				String propertyValue = group.getPropertyName().toString();
				if (temName.equals(strings[0])) {
					propertyValue = propertyValue.substring(temName.length() + 1);
				}
				isOver = buildCase(group, whereBufferHou, temName, propertyValue);

				break;
			} else {
				isOver = buildCase(group, whereBufferHou, temName, alisStr);
			}
		} // 遍历结束

		// 没有到最后（可能是 NULL/ NOT NULL）
		if (!isOver) {
			String matchCase = "";
			if (whereBufferHou.toString().trim().length() > 0) {
				matchCase = whereBufferHou.toString().trim().substring(whereBufferHou.toString().trim().length() - 1,
						whereBufferHou.toString().trim().length());
			}
			if (group.getMatchType() == MatchType.NULL || group.getMatchType() == MatchType.NOTNULL) {
				whereBufferHou.append(" ");
				if (group.getRelation() == MatchType.AND) {
					try {
						if (!matchCase.equals("(")) {
							whereBufferHou.append(" and ");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						if (!matchCase.equals("(")) {
							whereBufferHou.append(" or ");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				switch (group.getMatchType()) {
				case NOTIN:
					whereBufferHou.append(temName).append(" not in  ");

					break;
				case NOTNULL:
					whereBufferHou.append(temName).append(" is not null ");
					break;

				case NULL:
					whereBufferHou.append(temName).append(" is null ");
					break;
				}
			} else {
				System.out.println("拼接处的name不能以对象结尾，统一要以基本类型结尾！");
			}
		}

		addValues(group, whereBufferHou, values);
	}

	private boolean buildCase(Group group, StringBuffer whereBufferHou, String temName, String alisStr) {
		boolean isOver;
		isOver = true;
		whereBufferHou.append(" ");
		String matchCase = "";
		if (whereBufferHou.toString().trim().length() > 0) {
			matchCase = whereBufferHou.toString().trim().substring(whereBufferHou.toString().trim().length() - 1,
					whereBufferHou.toString().trim().length());
		}
		if (group.getRelation() == MatchType.AND) {
			if (!matchCase.equals("(")) {
				whereBufferHou.append(" and ");
			}
		} else {
			if (!matchCase.equals("(")) {
				whereBufferHou.append(" or ");
			}
		}

		matchCase(group, whereBufferHou, temName, alisStr);

		return isOver;
	}

	@SuppressWarnings("incomplete-switch")
	private void matchCase(Group group, StringBuffer whereBufferHou, String temName, String alisStr) {
		switch (group.getMatchType()) {
		case EQ:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" = ");
			break;
		case LIKE:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" like ");

			break;

		case LT:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" < ");
			break;
		case LE:
			if (group.getRelation() == MatchType.AND)
				whereBufferHou.append(temName).append(".").append(alisStr).append(" <= ");
			break;
		case GT:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" > ");

			break;
		case GE:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" >= ");

			break;
		case NE:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" <> ");
			break;

		case IN:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" in ");
			break;
		case NOTIN:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" not in  ");

			break;
		case BETWEEN:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" between  ");

			break;
		case NULL:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" is null ");
			break;
		case NOTNULL:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" is not null ");
			break;
		}
	}

}
