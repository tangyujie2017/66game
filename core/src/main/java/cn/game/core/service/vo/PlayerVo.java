package cn.game.core.service.vo;

import java.util.Date;

import cn.game.core.entity.table.play.Player;
import cn.game.core.entity.table.play.PlayerAccount;

public class PlayerVo {



	private Long userId;
	// 授权

	private String authCode;// getToken 接口需传入此值，用于换取 accessToken
	// 获得token信息（需要登录授权成功）

	private String wxAccessToken;// 字符串类型；接口调用凭证，传给 getUserInfo 接口 获取用户信息；有效期2小时

	private String wxDynamicToken;// 字符串类型；当 accessToken 过期时把该值传给 refreshToken
									// 接口刷新 accessToken 的有效期。dynamicToken
									// 的有效期为30天

	private Long wxExpires;// 数字类型；accessToken 有效期，单位（秒）

	private String wxOpenId;// 普通用户的标识，对当前开发者帐号唯一

	private String wxNickname;// 普通用户昵称

	private String wxHeadimgurl;// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空

	private String wxUnionid;// 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的

	private String invitationNum;// 邀请码，用于代理结算

	private Boolean isLock;

	private PlayerAccountVo accout;

	private Date createTime;

	public Boolean getIsLock() {
		return isLock;
	}

	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getWxAccessToken() {
		return wxAccessToken;
	}

	public void setWxAccessToken(String wxAccessToken) {
		this.wxAccessToken = wxAccessToken;
	}

	public String getWxDynamicToken() {
		return wxDynamicToken;
	}

	public void setWxDynamicToken(String wxDynamicToken) {
		this.wxDynamicToken = wxDynamicToken;
	}

	public Long getWxExpires() {
		return wxExpires;
	}

	public void setWxExpires(Long wxExpires) {
		this.wxExpires = wxExpires;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public String getWxNickname() {
		return wxNickname;
	}

	public void setWxNickname(String wxNickname) {
		this.wxNickname = wxNickname;
	}

	public String getWxHeadimgurl() {
		return wxHeadimgurl;
	}

	public void setWxHeadimgurl(String wxHeadimgurl) {
		this.wxHeadimgurl = wxHeadimgurl;
	}

	public String getWxUnionid() {
		return wxUnionid;
	}

	public void setWxUnionid(String wxUnionid) {
		this.wxUnionid = wxUnionid;
	}

	public String getInvitationNum() {
		return invitationNum;
	}

	public void setInvitationNum(String invitationNum) {
		this.invitationNum = invitationNum;
	}

	public PlayerAccountVo getAccout() {
		return accout;
	}

	public void setAccout(PlayerAccountVo accout) {
		this.accout = accout;
	}

	public static PlayerVo playerToVo(Player player) {
		PlayerVo vo = new PlayerVo();
		vo.setAuthCode(player.getAuthCode());
		vo.setCreateTime(player.getCreateTime());
		vo.setInvitationNum(player.getInvitationNum());
		vo.setIsLock(player.getIsLock());
		vo.setUserId(player.getUserId());
		vo.setWxAccessToken(player.getWxAccessToken());
		vo.setWxDynamicToken(player.getWxDynamicToken());

		vo.setWxExpires(player.getWxExpires());
		vo.setWxHeadimgurl(player.getWxHeadimgurl());
		vo.setWxNickname(player.getWxNickname());
		vo.setWxOpenId(player.getWxOpenId());
		vo.setWxUnionid(player.getWxUnionid());
		PlayerAccountVo accountVo = new PlayerAccountVo();
		accountVo.setAccout_id(player.getAccout().getAccout_id());
		accountVo.setHistoryPayRecord(player.getAccout().getHistoryPayRecord());
		accountVo.setTotalGold(player.getAccout().getTotalGold());
		vo.setAccout(accountVo);
		return vo;

	}
	
}
