package cn.tz.www.customer.controller.service;

import java.util.List;

import cn.tz.www.customer.entity.table.Slide;
import cn.tz.www.customer.view.SliderVo;

public interface SliderService {
public List<SliderVo> loadSliderByType(Integer type,String imgUrl);
}
