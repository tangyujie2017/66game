package cn.game.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.game.api.controller.req.BaseRequest;
import cn.game.api.controller.req.BaseResponse;
import cn.game.api.controller.req.logic.PlayReq;
import cn.game.api.service.logic.GameLogicCenterService;

public class GameLogicController {
	private static Logger logger = Logger.getLogger(GameLoginController.class);
	@Autowired
	private GameLogicCenterService gameLogicCenterService;

	@PostMapping(value = "/api/v1/logic/play")
	@ResponseBody
	public ResponseEntity<BaseResponse> playGame(@RequestBody BaseRequest<PlayReq> req) {
		logger.debug("用户押积分");
		if (req.getData().getUserId() != null && req.getData().getList() != null
				&& req.getData().getList().size() > 0) {
			try {
				String current_batch = gameLogicCenterService.palyReady(req.getData().getUserId(),
						req.getData().getList());
				return BaseResponse.success(current_batch);
			} catch (Exception e) {

				e.printStackTrace();
				logger.error("玩家加入游戏出错id="+req.getData().getUserId()+e.getMessage());
				return BaseResponse.systemError("加入游戏报错了");
			}

		} else {

			return BaseResponse.systemError("请求参数错误");
		}

	}
}
