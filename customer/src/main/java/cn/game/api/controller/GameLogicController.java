package cn.game.api.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.game.api.controller.req.BaseRequest;
import cn.game.api.controller.req.BaseResponse;
import cn.game.api.controller.req.login.WXLoginReq;
import cn.game.api.service.login.GameLoginService;
import cn.game.core.entity.table.play.Player;

@Controller
public class GameLogicController {
	@Autowired
	private GameLoginService gameLoginService;
	@PostMapping(name="/api/v1/wx/wxLogin")
	@ResponseBody
	public ResponseEntity<BaseResponse> wxLogin(@ Valid @RequestBody BaseRequest<WXLoginReq> req,BindingResult result){
		if (result.hasErrors()) {
			return	BaseResponse.systemError("请求参数错误");
		}
		//检查用户在数据库是否存在
		
		Player player=gameLoginService.checkWxUser(req.getData().getWxUnionid());
		if(player.getUserId()==null){
			//说明此微信没有存在数据库
			 player =new Player();
			 player.setAuthCode(req.getData().getAuthCode());
			 player.setInvitationNum(req.getData().getInvitationNum()==null?"":req.getData().getInvitationNum());
			 player.setWxAccessToken(req.getData().getWxAccessToken());
			 player.setWxDynamicToken(req.getData().getWxDynamicToken());
			 player.setWxExpires(req.getData().getWxExpires());
			 player.setWxHeadimgurl(req.getData().getWxHeadimgurl());
			 player.setWxNickname(req.getData().getWxNickname());
			 player.setWxOpenId(req.getData().getWxOpenId());
			 player.setWxUnionid(req.getData().getWxUnionid());
			 player.setIsLock(false);
			 player.setCreateTime(new Date());
			 player= gameLoginService.saveWxUser(player);
			 return BaseResponse.success(player);
		}else{
			return BaseResponse.success(player);
		}
		
		
	}
	@PostMapping(name="/api/v1/wx/refresh ")
	@ResponseBody
	public ResponseEntity<BaseResponse> wxRefresh (@ Valid @RequestBody BaseRequest<WXLoginReq> req,BindingResult result){
		if (result.hasErrors()) {
			return	BaseResponse.systemError("请求参数错误");
		}
		//检查用户在数据库是否存在
		if(req.getData().getUserId()==null){
			return	BaseResponse.systemError("请求参数错误");
		}
		 //说明此微信没有存在数据库
		 Player player =new Player();
		 player.setUserId(req.getData().getUserId());
		 player.setAuthCode(req.getData().getAuthCode());
		 player.setInvitationNum(req.getData().getInvitationNum()==null?"":req.getData().getInvitationNum());
		 player.setWxAccessToken(req.getData().getWxAccessToken());//两小时修改一次
		 player.setWxDynamicToken(req.getData().getWxDynamicToken());//一个月修改一次
		 player.setWxExpires(req.getData().getWxExpires());
		 player.setWxHeadimgurl(req.getData().getWxHeadimgurl());
		 player.setWxNickname(req.getData().getWxNickname());
		 player.setWxOpenId(req.getData().getWxOpenId());
		 player.setWxUnionid(req.getData().getWxUnionid());
		 player= gameLoginService.saveWxUser(player);
		 return BaseResponse.success(player);
		
		
	}
}
