package cn.game.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.game.api.controller.req.BaseRequest;
import cn.game.api.controller.req.wallet.ApiRechargeReq;
import cn.game.api.controller.resp.BaseResponse;
import cn.game.api.service.wallet.GameWalletService;

@Controller
public class GameWalletController {
	@Autowired
	private GameWalletService gameWalletService;

	@PostMapping(value = "/api/v1/wx/recharge")
	@ResponseBody
	public ResponseEntity<BaseResponse> recharge(@Valid @RequestBody BaseRequest<ApiRechargeReq> req,
			BindingResult result) {
		if (result.hasErrors()) {
			return BaseResponse.systemError("请求参数错误");
		}
		boolean result1 = gameWalletService.recharge(req.getData().getUserId(), req.getData().getRechargeAmount());

		if (result1) {
			return BaseResponse.success("申请充值成功");
		} else {
			return BaseResponse.systemError("申请充值失败");
		}

	}

}
