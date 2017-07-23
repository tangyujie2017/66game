package cn.game.api.service.arithmetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
	public static void main(String[] args) {

		try {
			//AnimalUtil.receiver("1231456", 1l, createAnimalList(),null);
//			AnimalUtil.receiver("1231456", 2l, createAnimalList());
//			AnimalUtil.receiver("1231456", 3l, createAnimalList());
//			AnimalUtil.receiver("1231456", 4l, createAnimalList());
//
//			AnimalUtil.receiver("1231456", 5l, createAnimalList());
			
			//AnimalUtil.matcher("1231456",null);
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private static List<Animal> createAnimalList() {

		Animal swallow = new Animal(1l, "燕子", 10 * createRandom(), 4, "swallow");
		Animal pigeon = new Animal(2l, "鸽子", 10 * createRandom(), 8, "pigeon");
		Animal peacock = new Animal(3l, "孔雀", 10 * createRandom(), 12, "peacock");
		Animal eagle = new Animal(4l, "老鹰", 10 * createRandom(), 24, "eagle");

//		// 走兽类
		Animal rabbit = new Animal(5l, "兔子", 10 * createRandom(), 4, "rabbit");
		Animal monkey = new Animal(6l, "猴子", 10 * createRandom(), 8, "monkey");
		Animal panda = new Animal(7l, "熊猫", 10 * createRandom(), 12, "panda");
		Animal tiger = new Animal(8l, "老虎", 10 * createRandom(), 24, "tiger");
		// boos
		Animal shark = new Animal(9l, "鲨鱼", 10 * createRandom(), 48, "shark");

		// 集合类
		Animal birds = new Animal(10l, "飞禽", 10 * createRandom(), 2, "birds");
		Animal beast = new Animal(11l, "走兽", 10 * createRandom(), 2, "beast");
		List<Animal> list = new ArrayList<>();
		list.add(swallow);
		list.add(pigeon);
		list.add(peacock);
		list.add(eagle);
		list.add(rabbit);

		list.add(rabbit);
		list.add(monkey);

		list.add(panda);
		list.add(tiger);
		list.add(shark);
		list.add(birds);
		list.add(beast);

		return list;

	}

	private static int createRandom() {
		int max = 2000;
		int min = 0;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
}
