package cn.game.core.repository.redis;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valOpsStr;
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Resource(name = "redisTemplate")
	private ValueOperations<Object, Object> valOps;

	public void saveString(String key, String value) {
		valOpsStr.set(key, value);
	}

	public String getString(String key) {
		return valOpsStr.get(key);
	}

	public void saveObject(Object key, Object value) {
		valOps.set(key, value);
	}

	public Object getObject(Object key) {
		return valOps.get(key);
	}

}
