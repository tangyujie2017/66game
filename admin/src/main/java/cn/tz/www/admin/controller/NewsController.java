package cn.tz.www.admin.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.tz.www.admin.config.Config.Values;
import cn.tz.www.admin.controller.service.NewsService;
import cn.tz.www.admin.controller.service.UserService;
import cn.tz.www.admin.controller.util.NewsVo;
import cn.tz.www.admin.controller.util.PageParam;
import cn.tz.www.admin.controller.util.PageVo;
import cn.tz.www.customer.entity.table.News;
import cn.tz.www.customer.entity.table.Slide;
import cn.tz.www.customer.entity.tools.CommonUtil;
import cn.tz.www.customer.entity.tools.Constants;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.JsonObj;
import cn.tz.www.customer.entity.tools.Page;

@Controller
public class NewsController {

	@Autowired
	private NewsService newsService;
	@Autowired
	private UserService userService;
	@Autowired
	private Values values;
	@Autowired
	private ServletContext servletContext;
	@PostMapping("/news/create")
	@ResponseBody
	public JsonObj createNews(News news, Principal principal, HttpServletRequest request) {
		//
		if (principal == null) {
			return JsonObj.newErrorJsonObj("用户已过期请从新登录");
		}
		news.setCreateTime(new Date());
		news.setStatus(1);
		news.setViewTimes(createViewTimes());
	
		// 设置USer
		if (!principal.getName().equals("admin")) {
			news.setCreateUser(userService.loadUserByLogin(principal.getName()));
		}
		uploadImg( news,  request);
		newsService.createNews(news);
		return JsonObj.newSuccessJsonObj("创建消息成功");
	}
	private void uploadImg(News news, HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		String savePath = values.getUploadedImagesDir();
		if (CommonUtil.isNull(savePath)) {// 没有配置特定目录 就保存到temp目录
			savePath = servletContext.getRealPath("/") + "temp";
		}
		savePath += "/newsMianImg/";
		String extName = "";// 扩展名

		String newFileName = "";
		Iterator<String> it = multipartRequest.getFileNames();
		while (it.hasNext()) {
			String nowTime = CommonUtil.getNow(6, null);// 当前时间 毫秒数
			String fileName = it.next();
			MultipartFile uploadify = multipartRequest.getFile(fileName);

			String filename = uploadify.getOriginalFilename();
			File dir = new File(savePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (filename.lastIndexOf(".") >= 0) {
				extName = filename.substring(filename.lastIndexOf("."));
			}
			newFileName = nowTime + extName;

			File file = new File(savePath + newFileName);

			try {
				uploadify.transferTo(file);
			} catch (IllegalStateException | IOException e) {

				e.printStackTrace();
			}
			CommonUtil.thumbnailImage(file, Constants.THUMBNAIL_WIDTH, Constants.THUMBNAIL_HEIGHT, "small_", false);// 生成缩略图
			news.setNewsMainImg("newsMianImg/small_" + newFileName);

		}
	}
	@PostMapping("/news/delNews")
	@ResponseBody
	public JsonObj delNews(Long id, Principal principal) {
		//
		if (principal == null) {
			return JsonObj.newErrorJsonObj("用户已过期请从新登录");
		}

		newsService.delNews(id);
		// 设置USer

		return JsonObj.newSuccessJsonObj("删除消息成功");
	}

	private Integer createViewTimes() {
		int max = 2000;
		int min = 200;
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;

		return s;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/news/list")
	@ResponseBody
	public PageVo<NewsVo> listNews(@Valid PageParam param, BindingResult result, Principal principal) {
		//
		if (principal == null) {
			JsonObj.newErrorJsonObj("用户已过期请从新登录");
		}
		int pageSize = param.getPageSize();
		int currentPage = param.getPageIndex();
		Groups g = new Groups();
		g.Add("status", 1);
		g.setOrderby("createTime");
		Page<News> page = new Page<News>(pageSize, currentPage);
		newsService.newsList(g, page);
		PageVo<NewsVo> vo = new PageVo<>();
		vo.setTotal(page.getTotalCount());
		List<NewsVo> list=new ArrayList<>();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		page.getItems().stream().forEach((Consumer<?>)a->{
			News n=(News)a;
			NewsVo temp=new NewsVo();
			temp.setContext(n.getContext());
			temp.setCreateTime(time.format(n.getCreateTime()));
			if(n.getCreateUser()!=null){
				
				temp.setCreateUser(n.getCreateUser().getRealName()==null?"":n.getCreateUser().getRealName());
			}
		
			temp.setDeclareContext(n.getDeclareContext());
			temp.setId(n.getId());
			temp.setStatus(n.getStatus());
			temp.setTitle(n.getTitle());
			temp.setViewTimes(n.getViewTimes());
			list.add(temp);
		});
		vo.setItems(list);
		return vo;
	}

	@RequestMapping("/news/loadNewsById")
	@ResponseBody
	public JsonObj loadNewsById(Long newsId, Principal principal) {
		//
		if (principal == null) {
			JsonObj.newErrorJsonObj("用户已过期请从新登录");
		}
		News n=newsService.loadNewsById(newsId);
		NewsVo temp=new NewsVo();
		temp.setContext(n.getContext());
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		temp.setCreateTime(time.format(n.getCreateTime()));
		if(n.getCreateUser()!=null){
			
			temp.setCreateUser(n.getCreateUser().getRealName()==null?"":n.getCreateUser().getRealName());
		}
	
		temp.setDeclareContext(n.getDeclareContext());
		temp.setId(n.getId());
		temp.setStatus(n.getStatus());
		temp.setTitle(n.getTitle());
		temp.setViewTimes(n.getViewTimes());
		return JsonObj.newSuccessJsonObj("加载成功", temp);
	}
}
