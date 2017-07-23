package cn.game.api.service.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.jms.Queue;
import javax.jms.Topic;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import cn.game.api.controller.resp.logic.GameResultResp;
import cn.game.api.exception.BizException;
import cn.game.api.service.arithmetic.Animal;
import cn.game.api.service.arithmetic.AnimalConstant;
import cn.game.api.service.arithmetic.TypeOfAnimal;
import cn.game.api.service.arithmetic.UserAnimal;
import cn.game.api.service.mq.Email;
import cn.game.core.entity.table.play.BaseAnimal;
import cn.game.core.entity.table.play.GameAnimal;
import cn.game.core.entity.table.play.Player;
import cn.game.core.entity.table.play.PlayerAccount;
import cn.game.core.entity.table.play.PlayerGame;
import cn.game.core.entity.table.play.PlayerGameResult;
import cn.game.core.entity.table.play.ResultAnimal;
import cn.game.core.entity.table.play.UserGameResult;
import cn.game.core.repository.play.PlayerAccountRepository;
import cn.game.core.repository.play.PlayerGameRepository;
import cn.game.core.repository.play.PlayerGameResultRepository;
import cn.game.core.repository.play.PlayerRepository;
import cn.game.core.repository.redis.RedisRepository;
import cn.game.core.service.vo.GameResultVo;
import cn.game.core.service.vo.UserGameResultVo;

@Service
public class GameLogicCenterServiceImpl implements GameLogicCenterService {
	private static Logger logger = Logger.getLogger(GameLogicCenterServiceImpl.class);
	@Autowired
	private RedisRepository redisRepository;
	@Autowired
	private PlayerGameRepository playerGameRepository;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private PlayerAccountRepository playerAccountRepository;
	@Autowired
	private PlayerGameResultRepository playerGameResultRepository;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Queue queue;

	@Autowired
	private Topic topic;

	// ------------------

	private void initBatch(String batch) {
		// 初始化批次号
		System.out.println("初始化批次号_从缓存中拿到的批次为" + batch);
		Map<Long, List<Animal>> map = new HashMap<Long, List<Animal>>();

		Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;
		if (container.get(batch) == null) {
			logger.debug("设置游戏接收数据容器container[AnimalConstant.Data_Container]");
			container.put(batch, map);
		}
	}

	// 初始化每一局的基础数据
	private void initTypeOfAnimal() {
		logger.debug("初始化[AnimalConstant.TypeOfAnimal]");
		Map<String, TypeOfAnimal> gameInitData = AnimalConstant.TypeOfAnimal;
		gameInitData.clear();
		gameInitData.put("swallow", new TypeOfAnimal(1l));
		gameInitData.put("pigeon", new TypeOfAnimal(2l));
		gameInitData.put("peacock", new TypeOfAnimal(3l));
		gameInitData.put("eagle", new TypeOfAnimal(4l));

		gameInitData.put("rabbit", new TypeOfAnimal(5l));
		gameInitData.put("monkey", new TypeOfAnimal(6l));
		gameInitData.put("panda", new TypeOfAnimal(7l));
		gameInitData.put("tiger", new TypeOfAnimal(8l));

		gameInitData.put("shark", new TypeOfAnimal(9l));
		// 飞禽
		gameInitData.put("birds", new TypeOfAnimal(10l));
		// 走兽
		gameInitData.put("beast", new TypeOfAnimal(11l));

	}

	@Override
	@Transactional
	public String receiver(Long userId, List<Animal> list) throws Exception {
		String current_batch = redisRepository.getString("current_batch");
		this.initBatch(current_batch);
		this.initTypeOfAnimal();
		// batch 怎么来？

		logger.debug("用户userId：" + userId + " 买入游戏批次号batch：" + current_batch + " 买入详情：");
		Long totalScore = 0l;
		List<GameAnimal> aList = new ArrayList<>();
		for (Animal a : list) {
			GameAnimal ga = new GameAnimal();
			logger.debug("...动物[id_名字]：" + a.getId() + "_" + a.getName() + " 买入分数：" + a.getScore() + " 中奖赔偿分数[买入*赔率]"
					+ "[" + a.getScore() + "*" + a.getMultiple() + "]=" + a.getScore() * a.getMultiple());

			ga.setScore(a.getScore());
			ga.setTotalScore((long) (a.getScore() * a.getMultiple()));
			BaseAnimal animal = new BaseAnimal();
			animal.setId(a.getId());
			aList.add(ga);
			ga.setAnimal(animal);
			totalScore += a.getScore();
		}
		if (current_batch != null && !current_batch.equals("") && userId != null && list != null && list.size() > 0) {
			Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;

			Map<Long, List<Animal>> userMap = container.get(current_batch);

			if (userMap != null) {
				if (userMap.get(userId) != null && userMap.get(userId).size() > 0) {

					throw new BizException("你已经提交过数据,请稍后！");
				} else {
					logger.debug("用户userId：" + userId + "的数据加入数据容器[AnimalConstant.Data_Container]");
					userMap.put(userId, list);
					// 保存用户购买数据进入数据库
					PlayerGame playerGame = new PlayerGame();
					playerGame.setBatchNum(current_batch);
					playerGame.setCreateTime(new Date());
					Player p = new Player();
					p.setUserId(userId);
					playerGame.setPlayer(p);
					playerGame.setTotalScore(totalScore);
					playerGame.setAnimalList(aList);
					playerGameRepository.persist(playerGame);
				}
				container.put(current_batch, userMap);
			} else {
				throw new BizException("batch已过期稍后重试");
			}
		} else {

			throw new BizException("提交参数有异常,请检查后再提交");
		}
		return current_batch;
	}

	/**
	 * 定时器任务每隔一定时间开启一批次的游戏结果
	 * 
	 */
	@Override
	@Transactional
	public void matcher(String batch) {
		logger.debug("开始进行匹配 匹配的批次号：" + batch);
		Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;
		if (batch != null && !batch.equals("")) {
			Map<Long, List<Animal>> userMap = container.get(batch);
			// 删除批次号
			container.remove(batch);

			logger.debug("在[AnimalConstant.Data_Container]删除批次号：" + batch);
			logger.debug("检查当前批次" + batch + "是否存在[AnimalConstant.Data_Container]中"
					+ (container.get(batch) == null ? false : true));
			if (userMap != null) {
				// 得到用户数据
				Map<Long, UserAnimal> userDate = userAnimalDateFormat(userMap);

				Map<String, TypeOfAnimal> result = sumTotal(userMap, userDate);

				// 计算在内存中的总体值
				playReslt(result, batch, userDate);
			} else {
				// 插入当前批次日志
			}
		} else {

			throw new BizException("提交参数有异常,请检查后再提交");
		}

	}

	// 每一个用户详情
	private Map<Long, UserAnimal> userAnimalDateFormat(Map<Long, List<Animal>> userMap) {

		Set<Long> userids = userMap.keySet();
		// 用户id 购买的用户
		Map<Long, UserAnimal> map = new HashMap<>();

		for (Long userid : userids) {

			UserAnimal uGoods = new UserAnimal();
			uGoods.setUserid(userid);

			List<Animal> list = userMap.get(userid);
			Map<Long, Animal> animalMap = new HashMap<>();
			int inTotalScore = 0;
			for (Animal animal : list) {
				// 每一个用户商品map
				inTotalScore += animal.getScore();
				animalMap.put(animal.getId(), animal);

			}
			uGoods.setInTotalScore(inTotalScore);
			uGoods.setOutTotalScore(0);// 此时不知道开奖无法计算出用户赔偿多少
			uGoods.setAnimalMap(animalMap);
			map.put(userid, uGoods);
		}
		return map;
	}

	// 同一批次计算总和汇总
	private Map<String, TypeOfAnimal> sumTotal(Map<Long, List<Animal>> userMap, Map<Long, UserAnimal> userDate) {

		Map<String, TypeOfAnimal> gameInitData = AnimalConstant.TypeOfAnimal;
		if (gameInitData != null) {
			Set<Long> userids = userMap.keySet();

			for (Long userid : userids) {
				UserAnimal ua = userDate.get(userid);
				if (userDate.get(userid) != null) {

				}
				List<Animal> list = userMap.get(userid);
				for (Animal animal : list) {
					TypeOfAnimal tAnimal = gameInitData.get(animal.getType());
					if (tAnimal != null) {
						tAnimal.setIncome(tAnimal.getIncome() + animal.getScore());
						tAnimal.setAnimalId(animal.getId());
						tAnimal.setTotal(tAnimal.getTotal() + (animal.getScore() * animal.getMultiple()));
						if (!tAnimal.getUserAnimalList().contains(ua)) {
							tAnimal.getUserAnimalList().add(ua);
						}
					}
				}
			}
		}

		return gameInitData;
	}

	public void playReslt(Map<String, TypeOfAnimal> dataIn, String batchNum, Map<Long, UserAnimal> userDate) {
		// 只开一个
		Integer toatalScore = 0;
		Integer toplayerSum = 0;
		String animalType = "";

		// 飞禽数据
		Map<String, TypeOfAnimal> birds = new HashMap<>();
		birds.put("birds", dataIn.get("birds"));
		// 走兽数据
		Map<String, TypeOfAnimal> beast = new HashMap<>();
		beast.put("beast", dataIn.get("beast"));

		// 为0的数据(如果有为0的优先开这里面的)
		Map<String, TypeOfAnimal> zero = new HashMap<>();

		// key为动物type ，购买此动物id的所有uGoodsList
		for (String key : dataIn.keySet()) {
			// 计算总收入积分
			toatalScore += dataIn.get(key).getIncome();

			// 找支出
			// 排除飞禽与走兽的支出
			if (dataIn.get(key).getAnimalId() != 10l && dataIn.get(key).getAnimalId() != 11l) {
				// 开奖逻辑
				if (dataIn.get(key).getAnimalId() == 1l || dataIn.get(key).getAnimalId() == 2l
						|| dataIn.get(key).getAnimalId() == 3l || dataIn.get(key).getAnimalId() == 4l) {
					// 飞禽类
					// 寻找飞禽压0的数据
					int feiQin = birds.get("birds").getTotal();
					int currentAnimal = dataIn.get(key).getTotal();
					if (feiQin + currentAnimal == 0) {
						zero.put(key, dataIn.get(key));
					}

					if (toplayerSum == 0) {
						toplayerSum = dataIn.get(key).getTotal() + feiQin;
						animalType = key;
					} else {

						if (toplayerSum > (dataIn.get(key).getTotal() + feiQin)) {

							toplayerSum = dataIn.get(key).getTotal() + feiQin;
							animalType = key;
						}
					}

				} else if (dataIn.get(key).getAnimalId() == 5l || dataIn.get(key).getAnimalId() == 6l
						|| dataIn.get(key).getAnimalId() == 7l || dataIn.get(key).getAnimalId() == 8l) {

					// 走兽类
					// 寻找走兽压0的数据
					int zouShou = beast.get("beast").getTotal();
					int currentAnimal = dataIn.get(key).getTotal();
					if (zouShou + currentAnimal == 0) {
						zero.put(key, dataIn.get(key));
					}

					// 走兽类
					if (toplayerSum == 0) {
						toplayerSum = dataIn.get(key).getTotal() + zouShou;
						animalType = key;
					} else {
						//
						if (toplayerSum > (dataIn.get(key).getTotal() + zouShou)) {

							toplayerSum = dataIn.get(key).getTotal();
							animalType = key;
						}
					}

				} else {
					// 鲨鱼类
					// 鲨鱼0的数据

					int currentAnimal = dataIn.get(key).getTotal();
					if (currentAnimal == 0) {
						zero.put(key, dataIn.get(key));
					}

					if (toplayerSum == 0) {
						toplayerSum = dataIn.get(key).getTotal();
					} else {
						// 除了开鲨鱼以外其他都要加上计算飞禽，走兽数据
						if (toplayerSum > dataIn.get(key).getTotal()) {

							toplayerSum = dataIn.get(key).getTotal();
							animalType = key;
						}
					}
				}

			}
			// 排除飞禽和走兽

		}
		// 开奖
		// 为0的队列不为空时开这里面的结果
		String resultType = "";
		Long animalId = null;
		List<String> typeList = new ArrayList<>();
		for (String key : zero.keySet()) {
			typeList.add(key);
		}
		if (typeList.size() != 0) {
			int random = createRandom(typeList.size());
			resultType = typeList.get(random);
			animalId = dataIn.get(resultType).getAnimalId();

		} else {
			// 开取最小值结果
			resultType = animalType;
			animalId = dataIn.get(animalType).getAnimalId();
		}
		// 获取开奖结果结束

		logger.debug(
				"此局购买飞禽的情况[买入_中奖后支出][" + birds.get("birds").getIncome() + "_" + birds.get("birds").getTotal() + "]");
		logger.debug(
				"此局购买走兽的情况[买入_中奖后支出][" + beast.get("beast").getIncome() + "_" + beast.get("beast").getTotal() + "]");
		logger.debug(" 开奖结果Type：" + resultType + "   animalId：" + animalId);

		// 获得本次开出的结果
		TypeOfAnimal typeOfAnimal = dataIn.get(resultType);

		if (typeOfAnimal.getAnimalId().equals(1l) || typeOfAnimal.getAnimalId().equals(2l)
				|| typeOfAnimal.getAnimalId().equals(3l) || typeOfAnimal.getAnimalId().equals(4l)) {
			// 飞禽
			logger.debug("游戏结果如下：");
			logger.debug("...收入总分为：" + toatalScore + ",赔偿总分为[当前开奖动物_飞禽购买]：" + typeOfAnimal.getTotal() + "_"
					+ birds.get("birds").getTotal() + "共计："
					+ (typeOfAnimal.getTotal() + birds.get("birds").getTotal()));
			// --------------
			// 存储数据库

			PlayerGameResult result = new PlayerGameResult();
			BaseAnimal winner = new BaseAnimal();
			// 设置开奖的动物
			winner.setId(typeOfAnimal.getAnimalId());
			result.setAnimal(winner);
			result.setBatchNum(batchNum);
			result.setCreateTime(new Date());
			result.setIncomeTotalScore((long) toatalScore);
			result.setOutTotalScore((long) typeOfAnimal.getTotal() + birds.get("birds").getTotal());
			result.setResultTotalScore(
					(long) (toatalScore - (typeOfAnimal.getTotal() + birds.get("birds").getTotal())));
			if (toatalScore - (typeOfAnimal.getTotal() + birds.get("birds").getTotal()) > 0) {
				result.setType(0);
			} else {
				result.setType(1);
			}

			// --------------
			// 处理过的用户获奖情况

			// 获奖结果(普通类)
			logger.debug("获奖人名单如下：");
			for (UserAnimal userAnimal : typeOfAnimal.getUserAnimalList()) {
				logger.debug("获奖人id：" + userAnimal.getUserid());

				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					Animal animal = userAnimal.getAnimalMap().get(aID);
					if (typeOfAnimal.getAnimalId().equals(aID)) {

						UserAnimal temp = userDate.get(userAnimal.getUserid());
						// 设置当前用户赔偿
						temp.setOutTotalScore(animal.getScore() * animal.getMultiple());

						temp.getAnimalMap().get(aID).setSelected(true);

						int toUserScore = animal.getScore() * animal.getMultiple();
						logger.debug(" 赔偿积分：" + toUserScore + " ");

					}

				}

			}
			// 飞禽类
			TypeOfAnimal feiqin = birds.get("birds");
			for (UserAnimal userAnimal : feiqin.getUserAnimalList()) {
				logger.debug("飞禽类获奖人id：" + userAnimal.getUserid());

				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					Animal animal = userAnimal.getAnimalMap().get(aID);
					if (feiqin.getAnimalId().equals(aID)) {
						UserAnimal temp = userDate.get(userAnimal.getUserid());
						// 设置当前用户赔偿
						temp.setOutTotalScore(temp.getOutTotalScore() + (animal.getScore() * animal.getMultiple()));
						temp.getAnimalMap().get(aID).setSelected(true);

					}

				}
			}

			// userDate数据已经装完成
			// 1.推送消息JMS

			// 2.保存数据
			List<UserGameResult> uResult = new ArrayList<>();
			for (Long userid : userDate.keySet()) {
				UserAnimal userAnimal = userDate.get(userid);
				UserGameResult ugr = new UserGameResult();
				ugr.setOriginalScore(userAnimal.getInTotalScore());
				ugr.setResultScore(userAnimal.getOutTotalScore());
				Player player = new Player();
				player.setUserId(userid);
				ugr.setPlayer(player);
				List<ResultAnimal> rAnimalList = new ArrayList<>();
				for (Long animalid : userAnimal.getAnimalMap().keySet()) {
					Animal temp = userAnimal.getAnimalMap().get(animalid);
					ResultAnimal rTemp = new ResultAnimal();
					BaseAnimal ba = new BaseAnimal();
					ba.setId(animalid);
					rTemp.setAnimal(ba);
					rTemp.setOriginalScore(temp.getScore());
					if (temp.isSelected()) {
						rTemp.setResultScore(temp.getScore() * temp.getMultiple());
						rTemp.setSelected(true);
					} else {
						rTemp.setResultScore(0);
						rTemp.setSelected(false);
					}
					rAnimalList.add(rTemp);
					ugr.setDetails(rAnimalList);

				}
				// 更新用户的积分

				Player user = playerRepository.find(userid);
				PlayerAccount accout = user.getAccout();
				accout.setTotalGold(accout.getTotalGold() + (ugr.getResultScore() - ugr.getOriginalScore()));
				playerAccountRepository.update(accout);
				// ----
				uResult.add(ugr);

			}
			result.setResult(uResult);

			// 保存数据
			try {
				playerGameResultRepository.persist(result);
				// this.jmsTemplate.convertAndSend(this.topic, result);
			} catch (Exception e) {

				e.printStackTrace();
			}

		} else if (typeOfAnimal.getAnimalId().equals(5l) || typeOfAnimal.getAnimalId().equals(6l)
				|| typeOfAnimal.getAnimalId().equals(7l) || typeOfAnimal.getAnimalId().equals(8l)) {
			// 走兽
			logger.debug("游戏结果如下：");
			logger.debug("...收入总分为：" + toatalScore + ",赔偿总分为[当前开奖动物_走兽购买]：" + typeOfAnimal.getTotal() + "_"
					+ beast.get("beast").getTotal() + "共计："
					+ (typeOfAnimal.getTotal() + beast.get("beast").getTotal()));
			// 获奖结果
			logger.debug("获奖人名单如下：");

			PlayerGameResult result = new PlayerGameResult();
			BaseAnimal winner = new BaseAnimal();
			// 设置开奖的动物
			winner.setId(typeOfAnimal.getAnimalId());
			result.setAnimal(winner);
			result.setBatchNum(batchNum);
			result.setCreateTime(new Date());
			result.setIncomeTotalScore((long) toatalScore);
			result.setOutTotalScore((long) typeOfAnimal.getTotal() + beast.get("beast").getTotal());
			result.setResultTotalScore(
					(long) (toatalScore - (typeOfAnimal.getTotal() + beast.get("beast").getTotal())));
			if (toatalScore - (typeOfAnimal.getTotal() + beast.get("beast").getTotal()) > 0) {
				result.setType(0);
			} else {
				result.setType(1);
			}

			for (UserAnimal userAnimal : typeOfAnimal.getUserAnimalList()) {
				logger.debug("获奖人id：" + userAnimal.getUserid());

				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					if (typeOfAnimal.getAnimalId().equals(aID)) {
						Animal animal = userAnimal.getAnimalMap().get(aID);

						UserAnimal temp = userDate.get(userAnimal.getUserid());
						// 设置当前用户赔偿
						temp.setOutTotalScore(animal.getScore() * animal.getMultiple());

						temp.getAnimalMap().get(aID).setSelected(true);

						int toUserScore = animal.getScore() * animal.getMultiple();
						logger.debug(" 赔偿积分：" + toUserScore + " ");

					}

				}

			}
			// 走兽类
			TypeOfAnimal zouShou = beast.get("beast");
			for (UserAnimal userAnimal : zouShou.getUserAnimalList()) {
				logger.debug("走兽类获奖人id：" + userAnimal.getUserid());
				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					if (zouShou.getAnimalId().equals(aID)) {
						Animal animal = userAnimal.getAnimalMap().get(aID);
						UserAnimal temp = userDate.get(userAnimal.getUserid());
						// 设置当前用户赔偿
						temp.setOutTotalScore(temp.getOutTotalScore() + (animal.getScore() * animal.getMultiple()));
						temp.getAnimalMap().get(aID).setSelected(true);

					}

				}
			}
			// userDate数据已经装完成
			// 1.推送消息JMS

			// 2.保存数据
			List<UserGameResult> uResult = new ArrayList<>();
			for (Long userid : userDate.keySet()) {
				UserAnimal userAnimal = userDate.get(userid);
				UserGameResult ugr = new UserGameResult();
				ugr.setOriginalScore(userAnimal.getInTotalScore());
				ugr.setResultScore(userAnimal.getOutTotalScore());
				Player player = new Player();
				player.setUserId(userid);
				ugr.setPlayer(player);
				List<ResultAnimal> rAnimalList = new ArrayList<>();
				for (Long animalid : userAnimal.getAnimalMap().keySet()) {
					Animal temp = userAnimal.getAnimalMap().get(animalid);
					ResultAnimal rTemp = new ResultAnimal();
					BaseAnimal ba = new BaseAnimal();
					ba.setId(animalid);
					rTemp.setAnimal(ba);
					rTemp.setOriginalScore(temp.getScore());
					if (temp.isSelected()) {
						rTemp.setResultScore(temp.getScore() * temp.getMultiple());
						rTemp.setSelected(true);
					} else {
						rTemp.setResultScore(0);
						rTemp.setSelected(false);
					}
					rAnimalList.add(rTemp);
					ugr.setDetails(rAnimalList);

				}

				Player user = playerRepository.find(userid);
				PlayerAccount accout = user.getAccout();
				accout.setTotalGold(accout.getTotalGold() + (ugr.getResultScore() - ugr.getOriginalScore()));
				playerAccountRepository.update(accout);

				uResult.add(ugr);

			}
			result.setResult(uResult);

			// 保存数据
			try {
				playerGameResultRepository.persist(result);
				// this.jmsTemplate.convertAndSend(this.topic, result);
			} catch (Exception e) {

				e.printStackTrace();
			}

		} else {
			// 鲨鱼
			logger.debug("游戏结果如下：");
			logger.debug("...收入总分为：" + toatalScore + ",赔偿总分为[当前开奖动物]：" + typeOfAnimal.getTotal());
			// 获奖结果
			logger.debug("获奖人名单如下：");
			PlayerGameResult result = new PlayerGameResult();
			BaseAnimal winner = new BaseAnimal();
			// 设置开奖的动物
			winner.setId(typeOfAnimal.getAnimalId());
			result.setAnimal(winner);
			result.setBatchNum(batchNum);
			result.setCreateTime(new Date());
			result.setIncomeTotalScore((long) toatalScore);
			result.setOutTotalScore((long) typeOfAnimal.getTotal());
			result.setResultTotalScore((long) (toatalScore - typeOfAnimal.getTotal()));
			if (toatalScore - typeOfAnimal.getTotal() > 0) {
				result.setType(0);
			} else {
				result.setType(1);
			}

			for (UserAnimal userAnimal : typeOfAnimal.getUserAnimalList()) {
				logger.debug("获奖人id：" + userAnimal.getUserid());
				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					if (typeOfAnimal.getAnimalId().equals(aID)) {
						Animal animal = userAnimal.getAnimalMap().get(aID);
						UserAnimal temp = userDate.get(userAnimal.getUserid());
						// 设置当前用户赔偿
						temp.setOutTotalScore(temp.getOutTotalScore() + (animal.getScore() * animal.getMultiple()));
						temp.getAnimalMap().get(aID).setSelected(true);

					}

				}

			}

			// userDate数据已经装完成
			// 1.推送消息JMS

			// 2.保存数据
			List<UserGameResult> uResult = new ArrayList<>();
			for (Long userid : userDate.keySet()) {
				UserAnimal userAnimal = userDate.get(userid);
				UserGameResult ugr = new UserGameResult();
				ugr.setOriginalScore(userAnimal.getInTotalScore());
				ugr.setResultScore(userAnimal.getOutTotalScore());
				Player player = new Player();
				player.setUserId(userid);
				ugr.setPlayer(player);
				List<ResultAnimal> rAnimalList = new ArrayList<>();
				for (Long animalid : userAnimal.getAnimalMap().keySet()) {
					Animal temp = userAnimal.getAnimalMap().get(animalid);
					ResultAnimal rTemp = new ResultAnimal();
					BaseAnimal ba = new BaseAnimal();
					ba.setId(animalid);
					rTemp.setAnimal(ba);
					rTemp.setOriginalScore(temp.getScore());
					if (temp.isSelected()) {
						rTemp.setResultScore(temp.getScore() * temp.getMultiple());
						rTemp.setSelected(true);
					} else {
						rTemp.setResultScore(0);
						rTemp.setSelected(false);
					}
					rAnimalList.add(rTemp);
					ugr.setDetails(rAnimalList);

				}
				Player user = playerRepository.find(userid);
				PlayerAccount accout = user.getAccout();
				accout.setTotalGold(accout.getTotalGold() + (ugr.getResultScore() - ugr.getOriginalScore()));
				playerAccountRepository.update(accout);

				uResult.add(ugr);

			}
			result.setResult(uResult);

			// 保存数据
			try {
				playerGameResultRepository.persist(result);
				// this.jmsTemplate.convertAndSend(this.topic, result);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	private int createRandom(int max) {

		int min = 0;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	public GameResultResp loadUserPlayResult(String currentBatch, Long userId) {

		PlayerGameResult result = playerGameResultRepository.findUniqueBy("batchNum", currentBatch);
		// 说明已经开奖
		if (result != null) {
			
			GameResultResp resp = new GameResultResp();
			    //开奖信息
			GameResultVo gameResultVo=new GameResultVo();
			gameResultVo.setAninalId(result.getAnimal().getId());
			gameResultVo.setBatchNum(result.getBatchNum());
			gameResultVo.setId(result.getId());
			gameResultVo.setMultiple(result.getAnimal().getMultiple());
			gameResultVo.setName(result.getAnimal().getName());
			gameResultVo.setType(result.getAnimal().getType());
			resp.setResult(gameResultVo);
			List<UserGameResultVo> winner = new ArrayList<>();
			for (UserGameResult uResult : result.getResult()) {
				// 用户自己的
				if (uResult.getPlayer().getUserId().equals(userId)) {
					resp.setSelf(UserGameResultVo.userGameResultToVo(uResult));

				}

				// 所有用户中奖的信息
				if (uResult.getResultScore() > uResult.getOriginalScore()) {
					winner.add(UserGameResultVo.userGameResultToVo(uResult));
				}

			}
			return resp;

		}
		return null;

	}

}
