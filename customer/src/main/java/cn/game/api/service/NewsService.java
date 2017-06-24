package cn.game.api.service;



import cn.game.api.contorller.view.NewsVo;
import cn.tz.www.customer.entity.table.News;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;

public interface NewsService {
	public Page<NewsVo> loadNewsByType(Groups groups, Page<News> page,String imgUrl) ;
	public void updateViewTimes(Long newsId);
	public NewsVo getNewsById(Long newsId,String imgUrl);
}
