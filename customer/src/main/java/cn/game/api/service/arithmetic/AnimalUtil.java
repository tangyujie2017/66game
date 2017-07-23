package cn.game.api.service.arithmetic;

import java.util.*;

import javax.persistence.Column;

import org.apache.log4j.Logger;

import cn.game.api.GameApiApplication;
import cn.game.api.exception.BizException;
import cn.game.api.service.logic.GameLogicCenterService;
import cn.game.core.entity.table.play.BaseAnimal;
import cn.game.core.entity.table.play.GameAnimal;
import cn.game.core.entity.table.play.Player;
import cn.game.core.entity.table.play.PlayerGame;
import cn.game.core.entity.table.play.PlayerGameResult;
import cn.game.core.entity.table.play.ResultAnimal;
import cn.game.core.entity.table.play.UserGameResult;

public class AnimalUtil {
	private static Logger logger = Logger.getLogger(AnimalUtil.class);


	

	/**
	 * 定时器任务每隔一定时间开启一批次的游戏结果
	 * 
	 */
	public  void matcher(String batch, GameLogicCenterService gameLogicCenterService) {
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
				playReslt(result, gameLogicCenterService, batch, userDate);
			} else {
				// 插入当前批次日志
			}
		} else {

			throw new BizException("提交参数有异常,请检查后再提交");
		}

	}

	// 每一个用户详情
	private  Map<Long, UserAnimal> userAnimalDateFormat(Map<Long, List<Animal>> userMap) {

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
	private  Map<String, TypeOfAnimal> sumTotal(Map<Long, List<Animal>> userMap, Map<Long, UserAnimal> userDate) {

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

	public  void playReslt(Map<String, TypeOfAnimal> dataIn, GameLogicCenterService gameLogicCenterService,
			String batchNum, Map<Long, UserAnimal> userDate) {
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
				uResult.add(ugr);

			}
			result.setResult(uResult);

			// 保存数据
			try {
				//AnimalUtigameLogicCenterService.saveResultData(result);
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
				uResult.add(ugr);

			}
			result.setResult(uResult);

			// 保存数据
			try {
				//gameLogicCenterService.saveResultData(result);
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
				uResult.add(ugr);

			}
			result.setResult(uResult);

			// 保存数据
			try {
				//gameLogicCenterService.saveResultData(result);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	private  int createRandom(int max) {

		int min = 0;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

}
