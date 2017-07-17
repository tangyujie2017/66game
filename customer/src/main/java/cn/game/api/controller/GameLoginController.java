package cn.game.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameLoginController {
	@PostMapping(name = "/game/api/v1/login")
	public void appLogin() {

	}
	
	@PostMapping(name = "/game/api/v1/loginByid")
	public void appLoginById() {

	}
	
	
}
