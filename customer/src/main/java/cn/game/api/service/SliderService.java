package cn.game.api.service;

import java.util.List;

import cn.game.api.contorller.view.SliderVo;
import cn.tz.www.customer.entity.table.Slide;

public interface SliderService {
public List<SliderVo> loadSliderByType(Integer type,String imgUrl);
}
