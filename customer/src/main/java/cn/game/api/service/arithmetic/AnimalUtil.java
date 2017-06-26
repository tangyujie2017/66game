package cn.game.api.service.arithmetic;

import java.util.*;

import cn.game.api.exception.BizException;

public class AnimalUtil {
	private static void initBatch() {
		// 初始化批次号
		String batch = "1231456";// 从缓存中拿取批次号放入数据池
		Map<Long, List<Animal>> map = new HashMap<Long, List<Animal>>();

		Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;
		if (container.keySet().size() == 0) {
			container.put(batch, map);
		}
	}

	// 初始化每一局的基础数据
	private static void initTypeOfAnimal() {
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
		gameInitData.put("birds", new TypeOfAnimal(10l));
		gameInitData.put("beast", new TypeOfAnimal(11l));

	}

	public static void receiver(String batch, Long userId, List<Animal> list) throws Exception {
		AnimalUtil.initBatch();
		AnimalUtil.initTypeOfAnimal();
		// batch 怎么来？
		if (batch != null && !batch.equals("") && userId != null && list != null && list.size() > 0) {
			Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;

			Map<Long, List<Animal>> userMap = container.get(batch);

			if (userMap != null) {
				if (userMap.get(userId) != null && userMap.get(userId).size() > 0) {

					throw new BizException("你已经提交过数据,请稍后！");
				} else {

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

	public static void matcher(String batch) {

		Map<String, Map<Long, List<Animal>>> container = AnimalConstant.Data_Container;
		if (batch != null && !batch.equals("")) {
			Map<Long, List<Animal>> userMap = container.get(batch);
			// 删除批次号
			container.remove(batch);
			if (userMap != null) {
				// 得到用户数据
				Map<Long, UserAnimal> userDate = userAnimalDateFormat(userMap);

				Map<String, TypeOfAnimal> result = sumTotal(userMap, userDate);

				
				// 计算在内存中的总体值
				playReslt(result) ;
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
		// 商品id 购买的用户
		Map<Long, UserAnimal> map = new HashMap<>();

		for (Long userid : userids) {

			UserAnimal uGoods = new UserAnimal();
			uGoods.setUserid(userid);

			List<Animal> list = userMap.get(userid);
			Map<Long, Animal> goodsMap = new HashMap<>();
			for (Animal animal : list) {
				// 每一个用户商品map

				goodsMap.put(animal.getId(), animal);
				uGoods.setAnimalMap(goodsMap);

			}
			uGoods.setAnimalMap(goodsMap);
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
						if (!tAnimal.getuGoodsList().contains(ua)) {
							tAnimal.getuGoodsList().add(ua);
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
		Integer toplayer = 0;
		for (String key : dataIn.keySet()) {
			toatalScore += dataIn.get(key).getIncome();
			if (toplayer == 0) {
				toplayer = dataIn.get(key).getTotal();
			}else{
				System.out.println("赔率"+dataIn.get(key).getTotal());
				if(toplayer>dataIn.get(key).getTotal()){
					
					toplayer=dataIn.get(key).getTotal();
				}
			}
		}
      System.out.println("此局共压分："+toatalScore+"赔率为："+toplayer);
	}
}
