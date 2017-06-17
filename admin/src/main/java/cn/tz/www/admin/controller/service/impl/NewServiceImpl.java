package cn.tz.www.admin.controller.service.impl;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tz.www.admin.controller.service.NewsService;
import cn.tz.www.customer.entity.repository.news.NewsRepository;
import cn.tz.www.customer.entity.table.News;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;

@Service
public class NewServiceImpl implements NewsService {
	@Autowired
	private NewsRepository newsRepository;

	@Transactional
	public void createNews(News news) {
		newsRepository.persist(news);
	}
	@Transactional
	public void delNews(Long id) {
		newsRepository.delete(id);
	}

	public Page<News> newsList(Groups groups, Page<News> page) {

		return newsRepository.findEntityPageByGroups(groups, page);

	}

	public News loadNewsById(Long id) {
		return newsRepository.find(id);
	}

	
}
