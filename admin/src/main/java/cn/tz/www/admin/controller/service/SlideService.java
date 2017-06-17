package cn.tz.www.admin.controller.service;

import cn.tz.www.customer.entity.table.Slide;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;

public interface SlideService {
	public void createSlide(Slide slide);
	public Page<Slide> loadSlideList(Groups groups, Page<Slide> page);
	public Boolean checkSn(Integer sn,Integer type);
	public void delSlide(Long id);
	public Boolean totalSlideByType(Integer type);
}
