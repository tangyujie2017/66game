package cn.game.api.controller;

import java.util.Date;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.game.api.controller.req.BaseRequest;
import cn.game.api.controller.req.login.WXLoginReq;
import cn.game.api.controller.resp.BaseResponse;
import cn.game.api.service.login.GameLoginService;
import cn.game.core.entity.table.play.Player;
import cn.game.core.entity.table.play.PlayerAccount;
import cn.game.core.service.vo.PlayerVo;

@Controller
public class GameLoginController {
	private static Logger logger = Logger.getLogger(GameLoginController.class);
	@Autowired
	private GameLoginService gameLoginService;

	/**
	 * 用于微信用户登录
	 * 
	 **/
	@PostMapping(value = "/api/v1/wx/wxLogin")
	@ResponseBody
	public ResponseEntity<BaseResponse> wxLogin(@Valid @RequestBody BaseRequest<WXLoginReq> req, BindingResult result) {
		if (result.hasErrors()) {
			return BaseResponse.systemError("请求参数错误");
		}
		logger.debug("微信新用户登录");
		// 检查用户在数据库是否存在

		Player player = gameLoginService.checkWxUser(req.getData().getWxUnionid());
		if (player == null) {
			// 说明此微信没有存在数据库
			player = new Player();
			PlayerAccount account = new PlayerAccount();
			account.setHistoryPayRecord(0l);
			account.setPlayer(player);
			account.setTotalGold(200l);
			player.setAccout(account);
			player.setAuthCode(req.getData().getAuthCode());
			player.setInvitationNum(req.getData().getInvitationNum() == null ? "" : req.getData().getInvitationNum());
			player.setWxAccessToken(req.getData().getWxAccessToken());
			player.setWxDynamicToken(req.getData().getWxDynamicToken());
			player.setWxExpires(req.getData().getWxExpires());
			player.setWxHeadimgurl(req.getData().getWxHeadimgurl());
			player.setWxNickname(req.getData().getWxNickname());
			player.setWxOpenId(req.getData().getWxOpenId());
			player.setWxUnionid(req.getData().getWxUnionid());
			player.setIsLock(false);
			player.setCreateTime(new Date());

			player = gameLoginService.saveWxUser(player);
			PlayerVo vo = PlayerVo.playerToVo(player);
			return BaseResponse.success(vo);
		} else {
			if (req.getData().getInvitationNum() != null && !"".equals(req.getData().getInvitationNum())) {
				if(player.getInvitationNum()!=null&&!"".equals(player.getInvitationNum())){
					player.setInvitationNum(req.getData().getInvitationNum());
				}
				
			}
			player.setWxAccessToken(req.getData().getWxAccessToken());
			player.setWxDynamicToken(req.getData().getWxDynamicToken());
			gameLoginService.updateWxUser(player);
			PlayerVo vo = PlayerVo.playerToVo(player);
			return BaseResponse.success(vo);
		}

	}

	/**
	 * 用于微信用户刷新token或修改其他值
	 * 
	 **/
	@PostMapping(value = "/refresh ")
	@ResponseBody
	public ResponseEntity<BaseResponse> wxRefresh(@Valid @RequestBody BaseRequest<WXLoginReq> req,
			BindingResult result) {
		logger.debug("微信新用户刷新数据");
		if (result.hasErrors()) {
			return BaseResponse.systemError("请求参数错误");
		}
		// 检查用户在数据库是否存在
		if (req.getData().getUserId() == null) {
			return BaseResponse.systemError("请求参数错误");
		}
		// 说明此微信没有存在数据库
		Player player = new Player();
		player.setUserId(req.getData().getUserId());
		player.setWxAccessToken(req.getData().getWxAccessToken());// 两小时修改一次
		player.setWxDynamicToken(req.getData().getWxDynamicToken());// 一个月修改一次
		player.setWxHeadimgurl(req.getData().getWxHeadimgurl());
		player.setWxNickname(req.getData().getWxNickname());
		// player.setWxOpenId(req.getData().getWxOpenId());
		// player.setWxUnionid(req.getData().getWxUnionid());
		player = gameLoginService.saveWxUser(player);
		PlayerVo vo = PlayerVo.playerToVo(player);
		return BaseResponse.success(vo);

	}

}
