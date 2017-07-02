package cn.game.api.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonAutoConfiguration {

      //缓存属性
	  @Autowired private RedisProperties redisProperties;
	  
      //（缓存分布式锁）使用方式   RLock lock = redissonClient.getLock(lockKey);
	  @Bean
	  public RedissonClient redissonClient() {
	    Config config = new Config();
	    //sentinel(哨兵模式)
	    if (redisProperties.getSentinel() != null) {
	      SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
	      sentinelServersConfig.setMasterName(redisProperties.getSentinel().getMaster());
	      redisProperties.getSentinel().getNodes();
	      sentinelServersConfig.addSentinelAddress(redisProperties.getSentinel().getNodes().split(","));
	      sentinelServersConfig.setDatabase(redisProperties.getDatabase());
	      if (redisProperties.getPassword() != null) {
	        sentinelServersConfig.setPassword(redisProperties.getPassword());
	      }
	    } else { //single server
	      SingleServerConfig singleServerConfig = config.useSingleServer();
	      singleServerConfig.setAddress(redisProperties.getHost() + ":" + redisProperties.getPort());
	      singleServerConfig.setDatabase(redisProperties.getDatabase());
	      if (redisProperties.getPassword() != null) {
	        singleServerConfig.setPassword(redisProperties.getPassword());
	      }
	    }
	    return Redisson.create(config);
	  }

}
