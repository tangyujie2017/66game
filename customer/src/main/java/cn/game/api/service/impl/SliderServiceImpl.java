package cn.game.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.game.api.contorller.view.SliderVo;
import cn.game.api.service.SliderService;
import cn.game.core.repository.slide.SlideRepository;
import cn.game.core.table.Slide;
import cn.game.core.tools.Groups;

@Service
public class SliderServiceImpl implements SliderService {
	@Autowired
	private SlideRepository slideRepository;

	@Override
	public List<SliderVo> loadSliderByType(Integer type, String imgUrl) {
		Groups g = new Groups();
		 g.Add("type", type);
	     g.setOrderby("sn");
		List<Slide> list = slideRepository.findEntityByGroups(g);
		return convertNews(list, imgUrl);
	}

	private List<SliderVo> convertNews(List<Slide> list, String imgUrl) {
		List<SliderVo> vos = new ArrayList<SliderVo>();
		list.stream().forEach(a -> {
			SliderVo vo = new SliderVo();
			vo.setBaseimgUrl(imgUrl);
			vo.setId(a.getId());
			vo.setImgPath(a.getImgPath());
			vo.setRemark(a.getRemark());
			vo.setSn(a.getSn());
			vo.setType(1);
			vos.add(vo);
		});
		return vos;
	}

}
