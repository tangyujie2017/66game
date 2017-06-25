package cn.game.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import cn.game.core.Core;
@Import(Core.class)
@SpringBootApplication
//游戏接口启动类
public class GameApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameApiApplication.class, args);
	}
}
