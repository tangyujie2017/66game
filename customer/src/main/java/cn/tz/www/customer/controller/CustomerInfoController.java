package cn.tz.www.customer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tz.www.customer.controller.service.NewsService;
import cn.tz.www.customer.entity.table.News;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.JsonObj;
import cn.tz.www.customer.entity.tools.Page;

@Controller
public class CustomerInfoController {
	@Autowired
	private NewsService newsService;
	@Value("${customer.images.url-prefix}")
	private String imgUrl;
	// 注册
	@RequestMapping(value = "/api/customer/info/list")
	@ResponseBody
	public JsonObj getNewsByType(Integer type, Integer pageSize,Integer currentPage) {
		
		if (type != null&&pageSize!=null&&currentPage!=null) {
			
			Groups g = new Groups();
			g.Add("type", type);
			g.Add("status", 1);
			g.setOrderby("createTime");
			Page<News> page = new Page<News>(pageSize, currentPage);
			return JsonObj.newSuccessJsonObj("获取消息成功", newsService.loadNewsByType(g,page,imgUrl));
			
		}else{
			return JsonObj.newErrorJsonObj("请求参数不正确");
		}
		

	}

	@RequestMapping(value = "/api/customer/info/updateViews")
	@ResponseBody
	public JsonObj updateViews(Long newsId) {
		if (newsId == null) {
			return JsonObj.newErrorJsonObj("请求参数不正确");
		}
		newsService.updateViewTimes(newsId);
		return JsonObj.newSuccessJsonObj("更新浏览次数成功");

	}

	@RequestMapping(value = "/api/customer/info/getByid")
	@ResponseBody
	public JsonObj getNewsById(Long newsId) {
		if (newsId == null) {
			return JsonObj.newErrorJsonObj("请求参数不正确");
		}

		return JsonObj.newSuccessJsonObj("更新浏览次数成功", newsService.getNewsById(newsId,imgUrl));

	}
}
