package cn.tz.www.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tz.www.customer.controller.service.SliderService;
import cn.tz.www.customer.entity.tools.JsonObj;

@Controller
public class CustomerSliderController {
	
	@Autowired
	private SliderService sliderService;
	@Value("${customer.images.url-prefix}")
	private String imgUrl;
	
	// 注册
		@RequestMapping(value = "/api/customer/slider/list")
		@ResponseBody
		public JsonObj getSliderByType(Integer type) {
			if (type == null|| type.equals("")) {
				return JsonObj.newErrorJsonObj("请求参数不正确");
			}

			return JsonObj.newSuccessJsonObj("获取轮播图成功", sliderService.loadSliderByType(type,imgUrl));

		}
}
