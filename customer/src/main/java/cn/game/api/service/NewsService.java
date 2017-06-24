package cn.game.api.service;



import cn.game.api.contorller.view.NewsVo;
import cn.game.core.table.News;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

public interface NewsService {
	public Page<NewsVo> loadNewsByType(Groups groups, Page<News> page,String imgUrl) ;
	public void updateViewTimes(Long newsId);
	public NewsVo getNewsById(Long newsId,String imgUrl);
}
