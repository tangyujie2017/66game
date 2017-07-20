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
	private List<Animal> list;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<Animal> getList() {
		return list;
	}
	public void setList(List<Animal> list) {
		this.list = list;
	}
	
	
	
}
