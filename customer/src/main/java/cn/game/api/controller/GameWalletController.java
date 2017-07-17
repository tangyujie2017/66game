package cn.game.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameWalletController {

	@PostMapping(name = "/game/api/v1/pay")
	public void appScanerPay() {

	}
	@PostMapping(name = "/game/api/v1/takeOut")
	public void appTakeOut() {

	}
}
