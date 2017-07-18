package cn.game.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.game.api.service.logic.GameLogicCenterService;

@Controller(value="/api/v1/logic")
public class GameLogicController {
	private static Logger logger = Logger.getLogger(GameLoginController.class);
	@Autowired
	private GameLogicCenterService gameLogicCenterService;
	
	
	public void playGame() {
		
		
	}
}
