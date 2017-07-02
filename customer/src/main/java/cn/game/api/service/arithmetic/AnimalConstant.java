package cn.game.api.service.arithmetic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalConstant {
	// 用户游戏数据写入（批次＋用户对应下注）
	public static final Map<String, Map<Long, List<Animal>>> Data_Container = new HashMap<String, Map<Long, List<Animal>>>();
	// 基础类别
	public static final Map<Long, Animal> baseGoodsMap = new HashMap<Long, Animal>();
	public static final Map<String, TypeOfAnimal> TypeOfAnimal = new HashMap<String, TypeOfAnimal>();
	
//	static {
//		// 飞禽类
//		Animal swallow = new Animal(1l, "燕子", 10, 4, "swallow");
//		Animal pigeon = new Animal(2l, "鸽子", 10, 8, "pigeon");
//		Animal peacock = new Animal(3l, "孔雀", 10, 12, "peacock");
//		Animal eagle = new Animal(4l, "老鹰", 10, 24, "eagle");
//
//		// 走兽类
//		Animal rabbit = new Animal(5l, "兔子", 10, 4, "rabbit");
//		Animal monkey = new Animal(6l, "猴子", 10, 8, "monkey");
//		Animal panda = new Animal(7l, "熊猫", 10, 12, "panda");
//		Animal tiger = new Animal(8l, "老虎", 10, 24, "tiger");
//		// boos
//		Animal shark = new Animal(9l, "鲨鱼", 10, 48, "shark");
//
//		// 集合类
//		Animal birds = new Animal(10l, "飞禽", 10, 2, "birds");
//		Animal beast = new Animal(11l, "走兽", 10, 2, "beast");
//		baseGoodsMap.put(swallow.getId(), swallow);
//		baseGoodsMap.put(pigeon.getId(), pigeon);
//		baseGoodsMap.put(peacock.getId(), peacock);
//		baseGoodsMap.put(eagle.getId(), eagle);
//		baseGoodsMap.put(rabbit.getId(), rabbit);
//		baseGoodsMap.put(monkey.getId(), monkey);
//		baseGoodsMap.put(panda.getId(), panda);
//		baseGoodsMap.put(tiger.getId(), tiger);
//		baseGoodsMap.put(shark.getId(), shark);
//		baseGoodsMap.put(birds.getId(), birds);
//		baseGoodsMap.put(beast.getId(), beast);
//
//	}
	
}
