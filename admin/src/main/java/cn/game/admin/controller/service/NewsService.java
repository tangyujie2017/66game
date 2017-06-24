package cn.game.admin.controller.service;


import cn.game.core.table.News;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

public interface NewsService {
	public void createNews(News news);
	public Page<News>  newsList(Groups groups, Page<News> page);
	public News loadNewsById(Long id) ;
	public void delNews(Long id);
}
