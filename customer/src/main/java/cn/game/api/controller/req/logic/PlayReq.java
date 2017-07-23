package cn.game.api.controller.req.logic;

import java.io.Serializable;
import java.util.List;

import cn.game.api.service.arithmetic.Animal;

public class PlayReq implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String animaList;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAnimaList() {
		return animaList;
	}
	public void setAnimaList(String animaList) {
		this.animaList = animaList;
	}
	
	
	
	
	
}
