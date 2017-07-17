package cn.game.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameLogicController {
	@PostMapping(name = "/game/api/v1/addGame")
	public void addGame() {

	}
	
	@PostMapping(name = "/game/api/v1/loadGameByBacth")
	public void loadGameByBacth() {

	}
}
