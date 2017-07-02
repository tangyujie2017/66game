package cn.game.api.service.arithmetic;

import java.util.*;

import cn.game.api.exception.BizException;

public class AnimalUtil {
	private static void initBatch() {
		// 初始化批次号
		String batch = "1231456";// 从缓存中拿取批次号放入数据池
		System.out.println("初始化批次号_从缓存中拿到的批次为" + batch);
		Map<Long, List<Animal>> map = new HashMap<Long, List<Animal>>();

		Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;
		if (container.keySet().size() == 0) {
			System.out.println("设置游戏接收数据容器container[AnimalConstant.Data_Container]");
			container.put(batch, map);
		}
	}

	// 初始化每一局的基础数据
	private static void initTypeOfAnimal() {
		System.out.println("初始化[AnimalConstant.TypeOfAnimal]");
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

	public static void receiver(String batch, Long userId, List<Animal> list) throws Exception {
		AnimalUtil.initBatch();
		AnimalUtil.initTypeOfAnimal();
		// batch 怎么来？

		System.out.println("用户userId：" + userId + " 买入游戏批次号batch：" + batch + " 买入详情：");
		for (Animal a : list) {
			System.out.println(
					"...动物[id_名字]：" + a.getId() + "_" + a.getName() + " 买入分数：" + a.getScore() + " 中奖赔偿分数[买入*赔率]" + "["
							+ a.getScore() + "*" + a.getMultiple() + "]=" + a.getScore() * a.getMultiple());
		}
		if (batch != null && !batch.equals("") && userId != null && list != null && list.size() > 0) {
			Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;

			Map<Long, List<Animal>> userMap = container.get(batch);

			if (userMap != null) {
				if (userMap.get(userId) != null && userMap.get(userId).size() > 0) {

					throw new BizException("你已经提交过数据,请稍后！");
				} else {
					System.out.println("用户userId：" + userId + "的数据加入数据容器[AnimalConstant.Data_Container]");
					userMap.put(userId, list);
				}
				container.put(batch, userMap);
			} else {
				throw new BizException("batch已过期稍后重试");
			}
		} else {

			throw new BizException("提交参数有异常,请检查后再提交");
		}
	}

	/**
	 * 定时器任务每隔一定时间开启一批次的游戏结果
	 * 
	 */
	public static void matcher(String batch) {
		System.out.println("开始进行匹配 匹配的批次号：" + batch);
		Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;
		if (batch != null && !batch.equals("")) {
			Map<Long, List<Animal>> userMap = container.get(batch);
			// 删除批次号
			container.remove(batch);

			System.out.println("在[AnimalConstant.Data_Container]删除批次号：" + batch);
			System.out.println("检查当前批次" + batch + "是否存在[AnimalConstant.Data_Container]中"
					+ (container.get(batch) == null ? false : true));
			if (userMap != null) {
				// 得到用户数据
				Map<Long, UserAnimal> userDate = userAnimalDateFormat(userMap);

				Map<String, TypeOfAnimal> result = sumTotal(userMap, userDate);

				// 计算在内存中的总体值
				playReslt(result);
			} else {
				// 插入当前批次日志
			}
		} else {

			throw new BizException("提交参数有异常,请检查后再提交");
		}

	}

	// 每一个用户详情
	private static Map<Long, UserAnimal> userAnimalDateFormat(Map<Long, List<Animal>> userMap) {

		Set<Long> userids = userMap.keySet();
		// 用户id 购买的用户
		Map<Long, UserAnimal> map = new HashMap<>();

		for (Long userid : userids) {

			UserAnimal uGoods = new UserAnimal();
			uGoods.setUserid(userid);

			List<Animal> list = userMap.get(userid);
			Map<Long, Animal> animalMap = new HashMap<>();
			for (Animal animal : list) {
				// 每一个用户商品map

				animalMap.put(animal.getId(), animal);

			}
			uGoods.setAnimalMap(animalMap);
			map.put(userid, uGoods);
		}
		return map;
	}

	// 同一批次计算总和汇总
	private static Map<String, TypeOfAnimal> sumTotal(Map<Long, List<Animal>> userMap, Map<Long, UserAnimal> userDate) {
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

	public static void playReslt(Map<String, TypeOfAnimal> dataIn) {
		// 只开一个
		Integer toatalScore = 0;
		Integer toplayerSum = 0;
		String animalType = "";
		Map<String, TypeOfAnimal> toplayer = new HashMap<>();
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
		System.out.println(
				"此局购买飞禽的情况[买入_中奖后支出][" + birds.get("birds").getIncome() + "_" + birds.get("birds").getTotal() + "]");
		System.out.println(
				"此局购买走兽的情况[买入_中奖后支出][" + beast.get("beast").getIncome() + "_" + beast.get("beast").getTotal() + "]");
		System.out.println(" 开奖结果Type：" + resultType + "   animalId：" + animalId);
		// 开奖
		TypeOfAnimal typeOfAnimal = dataIn.get(resultType);
		if (typeOfAnimal.getAnimalId().equals(1l) || typeOfAnimal.getAnimalId().equals(2l)
				|| typeOfAnimal.getAnimalId().equals(3l) || typeOfAnimal.getAnimalId().equals(4l)) {
			// 飞禽
			System.out.println("游戏结果如下：");
			System.out.println("...收入总分为：" + toatalScore + ",赔偿总分为[当前开奖动物_飞禽购买]：" + typeOfAnimal.getTotal() + "_"
					+ birds.get("birds").getTotal() + "共计："
					+ (typeOfAnimal.getTotal() + birds.get("birds").getTotal()));
			// 获奖结果
			System.out.println("获奖人名单如下：");
			for (UserAnimal userAnimal : typeOfAnimal.getUserAnimalList()) {
				System.out.print("获奖人id：" + userAnimal.getUserid());
				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					if (typeOfAnimal.getAnimalId().equals(aID)) {
						Animal animal = userAnimal.getAnimalMap().get(aID);
						int toUserScore = animal.getScore() * animal.getMultiple();
						System.out.println(" 赔偿积分：" + toUserScore+" ");

					}

				}

			}
			
			//飞禽类
			TypeOfAnimal feiqin=birds.get("birds");
			for (UserAnimal userAnimal : feiqin.getUserAnimalList()) {
				System.out.print("飞禽类获奖人id：" + userAnimal.getUserid());
				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					if (feiqin.getAnimalId().equals(aID)) {
						Animal animal = userAnimal.getAnimalMap().get(aID);
						int toUserScore = animal.getScore() * animal.getMultiple();
						System.out.println(" 赔偿积分：" + toUserScore+" ");

					}

				}
			}
			

		} else if (typeOfAnimal.getAnimalId().equals(5l) || typeOfAnimal.getAnimalId().equals(6l)
				|| typeOfAnimal.getAnimalId().equals(7l) || typeOfAnimal.getAnimalId().equals(8l)) {
			// 走兽
			System.out.println("游戏结果如下：");
			System.out.println("...收入总分为：" + toatalScore + ",赔偿总分为[当前开奖动物_走兽购买]：" + typeOfAnimal.getTotal() + "_"
					+ beast.get("beast").getTotal() + "共计："
					+ (typeOfAnimal.getTotal() + beast.get("beast").getTotal()));
			// 获奖结果
			System.out.println("获奖人名单如下：");
			
			for (UserAnimal userAnimal : typeOfAnimal.getUserAnimalList()) {
				System.out.print("获奖人id：" + userAnimal.getUserid());
				
				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					if (typeOfAnimal.getAnimalId().equals(aID)) {
						Animal animal = userAnimal.getAnimalMap().get(aID);
						int toUserScore = animal.getScore() * animal.getMultiple();
						System.out.println(" 赔偿积分：" + toUserScore+" ");

					}

				}

			}
			//走兽类
			TypeOfAnimal zouShou=beast.get("beast");
			for (UserAnimal userAnimal : zouShou.getUserAnimalList()) {
				System.out.print("走兽类获奖人id：" + userAnimal.getUserid());
				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					if (zouShou.getAnimalId().equals(aID)) {
						Animal animal = userAnimal.getAnimalMap().get(aID);
						int toUserScore = animal.getScore() * animal.getMultiple();
						System.out.println(" 赔偿积分：" + toUserScore+" ");

					}

				}
			}
			
			
		} else {
			// 鲨鱼
			System.out.println("游戏结果如下：");
			System.out.println("...收入总分为：" + toatalScore + ",赔偿总分为[当前开奖动物]：" + typeOfAnimal.getTotal());
			// 获奖结果
			System.out.println("获奖人名单如下：");
			for (UserAnimal userAnimal : typeOfAnimal.getUserAnimalList()) {
				System.out.print("获奖人id：" + userAnimal.getUserid());
				for (Long aID : userAnimal.getAnimalMap().keySet()) {
					if (typeOfAnimal.getAnimalId().equals(aID)) {
						Animal animal = userAnimal.getAnimalMap().get(aID);
						int toUserScore = animal.getScore() * animal.getMultiple();
						System.out.println(" 赔偿积分：" + toUserScore+" ");

					}

				}

			}
		}
	}

	private static int createRandom(int max) {

		int min = 0;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
}
