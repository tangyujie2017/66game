package cn.game.api.service.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.game.api.service.arithmetic.AnimalUtil;
import cn.game.api.service.logic.GameLogicCenterService;
import cn.game.core.repository.redis.RedisRepository;

@Service
public class TashServiceImpl implements TaskService {
	@Resource
	private RedisRepository redisRepository;
	@Resource
	private GameLogicCenterService gameLogicCenterService;
	
	@Scheduled(fixedRate = 30000)
	public void generateBatchNo() {
		System.out.println("我正在生成批次号");
		String current_batch = redisRepository.getString("current_batch");
		//执行上一次的结果
		if (current_batch != null && !current_batch.equals("")) {
			gameLogicCenterService.matcher(current_batch);
		}
		redisRepository.saveString("current_batch", createBatch());

	}

	private String createBatch() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

}
