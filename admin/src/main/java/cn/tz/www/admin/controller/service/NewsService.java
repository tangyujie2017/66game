package cn.tz.www.admin.controller.service;


import cn.tz.www.customer.entity.table.News;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;

public interface NewsService {
	public void createNews(News news);
	public Page<News>  newsList(Groups groups, Page<News> page);
	public News loadNewsById(Long id) ;
	public void delNews(Long id);
}
