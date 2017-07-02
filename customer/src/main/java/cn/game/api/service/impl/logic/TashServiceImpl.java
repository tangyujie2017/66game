package cn.game.api.service.impl.logic;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.game.api.service.arithmetic.TaskService;
import cn.game.core.repository.redis.RedisRepository;

@Service
public class TashServiceImpl implements TaskService {
	@Resource
	private RedisRepository redisRepository;

	@Scheduled(fixedRate = 30000)
	public void generateBatchNo() {
		System.out.println("我正在生成批次号");
		redisRepository.saveString("current_batch", createBatch());

	}

	private String createBatch() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

}
