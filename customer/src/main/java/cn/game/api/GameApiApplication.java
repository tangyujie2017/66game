package cn.game.api;


import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import cn.game.core.Core;
@Import(Core.class)
@SpringBootApplication
//游戏接口启动类
public class GameApiApplication {
	private static Logger logger = Logger.getLogger(GameApiApplication.class);
	public static void main(String[] args) {
		logger.debug("游戏主程开始启动");
		SpringApplication.run(GameApiApplication.class, args);
	}
}
