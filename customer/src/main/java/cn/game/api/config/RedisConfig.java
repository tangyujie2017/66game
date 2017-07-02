package cn.game.api.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
//启用RedisHttpSession
@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 86400)
public class RedisConfig {
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.host}")
	private String redisUrl;

	@Bean
	public RedisConnectionFactory connectionFactory() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setPort(redisPort);
		connectionFactory.setHostName(redisUrl);
		return connectionFactory;
	}
	@Bean 
	
	public RedisTemplate<Object, Object> redisTemplate() throws UnknownHostException{
		
		RedisTemplate<Object, Object> template=new RedisTemplate<Object, Object>();
		template.setConnectionFactory(connectionFactory());
		//spring boot 默认用JdkSerializationRedisSerializer(二进制)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om=new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL,Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setStringSerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
		
	}
}
