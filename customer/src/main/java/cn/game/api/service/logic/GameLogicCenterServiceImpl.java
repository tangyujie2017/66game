package cn.game.api.service.logic;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.game.api.service.arithmetic.Animal;
import cn.game.api.service.arithmetic.AnimalUtil;
import cn.game.core.entity.table.play.PlayerGame;
import cn.game.core.entity.table.play.PlayerGameResult;
import cn.game.core.repository.play.PlayerGameRepository;
import cn.game.core.repository.play.PlayerGameResultRepository;
import cn.game.core.repository.redis.RedisRepository;
import cn.game.core.tools.Page;

@Service
public class GameLogicCenterServiceImpl implements GameLogicCenterService {
	@Autowired
	private RedisRepository redisRepository;
	@Autowired
	private PlayerGameRepository playerGameRepository;
	@Autowired
	private PlayerGameResultRepository playerGameResultRepository;
	
	
	@Override
	public void palyReady(Long userid, List<Animal> list) throws Exception {
		// 获得批次号
		String current_batch = redisRepository.getString("current_batch");
		// 存储数据
	

		// 加入队列
		AnimalUtil.receiver(current_batch, userid, list,this);

	}

	@Override
	@Transactional
	public void savePalyData(PlayerGame playerGame) throws Exception {
		playerGameRepository.persist(playerGame);
	}
	
	@Override
	@Transactional
	public void saveResultData(PlayerGameResult playerGameResult) throws Exception {
		playerGameResultRepository.persist(playerGameResult);
	}

}
