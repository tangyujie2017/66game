package cn.game.api.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.game.api.controller.req.BaseRequest;
import cn.game.api.controller.req.logic.GameResultReq;
import cn.game.api.controller.req.logic.PlayReq;
import cn.game.api.controller.resp.BaseResponse;
import cn.game.api.controller.resp.logic.GameResultResp;
import cn.game.api.service.arithmetic.Animal;
import cn.game.api.service.logic.GameLogicCenterService;
import cn.game.api.util.GameApiUtils;
import cn.game.core.entity.table.play.UserGameResult;
import cn.game.core.service.vo.UserGameResultVo;

@Controller
public class GameLogicController {
	private static Logger logger = Logger.getLogger(GameLoginController.class);
	@Autowired
	private GameLogicCenterService gameLogicCenterService;

	@PostMapping(value = "/api/v1/logic/play")
	@ResponseBody
	public ResponseEntity<BaseResponse> playGame(@RequestBody BaseRequest<PlayReq> req) {
		logger.debug("用户押积分");
		if (req.getData().getUserId() != null && req.getData().getAnimaList() != null
				&& !"".equals(req.getData().getAnimaList())) {
			try {
				List<Animal> list = GameApiUtils.json2listT(req.getData().getAnimaList(), Animal.class);

				String current_batch = gameLogicCenterService.receiver(req.getData().getUserId(), list);
				return BaseResponse.success(current_batch);
			} catch (Exception e) {

				e.printStackTrace();
				logger.error("玩家加入游戏出错id=" + req.getData().getUserId() + e.getMessage());
				return BaseResponse.business(e.getMessage());
			}

		} else {

			return BaseResponse.systemError("请求参数错误");
		}

	}

	@PostMapping(value = "/api/v1/logic/result")
	@ResponseBody
	public ResponseEntity<BaseResponse> gameResult(@RequestBody BaseRequest<GameResultReq> req, BindingResult result) {

		if (result.hasErrors()) {
			return BaseResponse.systemError("请求参数错误");
		}
		logger.debug("获取用户结果");
		if (req.getData().getUserId() != null && req.getData().getCurrentBatch() != null
				&& !"".equals(req.getData().getCurrentBatch())) {
			try {

				GameResultResp resp = gameLogicCenterService.loadUserPlayResult(req.getData().getCurrentBatch(),
						req.getData().getUserId());
				if (resp != null) {
					return BaseResponse.success(resp);
				} else {
					return BaseResponse.business("当前批次还没有开奖");

				}

			} catch (Exception e) {

				e.printStackTrace();
				logger.error("玩家获取游戏出错id=" + req.getData().getUserId() + e.getMessage());
				return BaseResponse.systemError("玩家获取游戏结果报错");
			}

		} else {

			return BaseResponse.systemError("请求参数错误");
		}

	}

	@RequestMapping(value = "/api/v1/logic/resultTest")
	@ResponseBody
	public ResponseEntity<BaseResponse> resultTest(String currentBatch, Long userId) {

		try {

			GameResultResp resp = gameLogicCenterService.loadUserPlayResult(currentBatch, userId);
			if (resp != null) {
				return BaseResponse.success(resp);
			} else {
				return BaseResponse.systemError("当前批次还没有开奖");

			}

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("玩家获取游戏出错id=" + userId + e.getMessage());
			return BaseResponse.systemError("玩家获取游戏结果报错");
		}

	}
}
