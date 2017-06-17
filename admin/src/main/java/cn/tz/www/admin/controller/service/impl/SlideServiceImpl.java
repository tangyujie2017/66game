package cn.tz.www.admin.controller.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tz.www.admin.controller.service.SlideService;
import cn.tz.www.customer.entity.repository.slide.SlideRepository;
import cn.tz.www.customer.entity.table.Slide;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;

@Service
public class SlideServiceImpl implements SlideService {
	@Autowired
	private SlideRepository slideRepository;
    @Transactional
	public void createSlide(Slide slide) {
		slideRepository.persist(slide);
	}
    @Transactional
  	public void delSlide(Long id) {
  		slideRepository.delete(id);
  	}

	@Override
	public Page<Slide> loadSlideList(Groups groups, Page<Slide> page) {
		return slideRepository.findEntityPageByGroups(groups, page);
	}

	public Boolean checkSn(Integer sn,Integer type) {
		Groups g = new Groups();
		g.Add("sn", sn);
		g.Add("type", type);
		List<Slide> list = slideRepository.findEntityByGroups(g);
		if (list != null && list.size() > 0) {
			return false;
		}
		return true;
	}
	public Boolean totalSlideByType(Integer type){
		Groups g = new Groups();
		g.Add("type", type);
		List<Slide> list = slideRepository.findEntityByGroups(g);
		//每一个类型只能有三个
		if(list!=null&&list.size() >= 3){
			return false;
		}else{
			return true;
		}
		
	}
}
