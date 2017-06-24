package cn.game.admin.controller.service.impl;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.game.admin.controller.service.NewsService;
import cn.game.core.repository.news.NewsRepository;
import cn.game.core.table.News;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

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
