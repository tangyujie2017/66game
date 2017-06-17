package cn.tz.www.customer.controller.service;



import cn.tz.www.customer.entity.table.News;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;
import cn.tz.www.customer.view.NewsVo;

public interface NewsService {
	public Page<NewsVo> loadNewsByType(Groups groups, Page<News> page,String imgUrl) ;
	public void updateViewTimes(Long newsId);
	public NewsVo getNewsById(Long newsId,String imgUrl);
}
