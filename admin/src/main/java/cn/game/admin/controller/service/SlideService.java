package cn.game.admin.controller.service;

import cn.game.core.table.Slide;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

public interface SlideService {
	public void createSlide(Slide slide);
	public Page<Slide> loadSlideList(Groups groups, Page<Slide> page);
	public Boolean checkSn(Integer sn,Integer type);
	public void delSlide(Long id);
	public Boolean totalSlideByType(Integer type);
}
