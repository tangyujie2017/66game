package cn.tz.www.admin.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;

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
import cn.tz.www.admin.controller.service.ProductService;
import cn.tz.www.admin.controller.util.PageParam;
import cn.tz.www.admin.controller.util.PageVo;
import cn.tz.www.customer.entity.table.Product;
import cn.tz.www.customer.entity.tools.CommonUtil;
import cn.tz.www.customer.entity.tools.Constants;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.JsonObj;
import cn.tz.www.customer.entity.tools.Page;

@Controller
public class ProductController {

	@Autowired
	private ServletContext servletContext;
	@Autowired
	private ProductService productService;

	@Autowired
	private Values values;
	@PostMapping("/product/create")
	@ResponseBody
	public JsonObj createProduct(Product product, Principal principal,HttpServletRequest request) {
		//
		if (principal == null) {
			return JsonObj.newErrorJsonObj("用户已过期请从新登录");
		}
		uploadImg(product, request);
		productService.saveProduct(product);
		

		return JsonObj.newSuccessJsonObj("创建产品成功");
	}
	private void uploadImg(Product product, HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		
		String savePath = values.getUploadedImagesDir();
		if (CommonUtil.isNull(savePath)) {// 没有配置特定目录 就保存到temp目录
			savePath = servletContext.getRealPath("/") + "temp";
		}
		savePath += "/product/";
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
			product.setProduct_img("product/small_" + newFileName);

		}
	}
	@PostMapping("/product/delProduct")
	@ResponseBody
	public JsonObj delProduct(Long id, Principal principal) {
		//
		if (principal == null) {
			return JsonObj.newErrorJsonObj("用户已过期请从新登录");
		}

		productService.deleteProduct(id);
		// 设置USer

		return JsonObj.newSuccessJsonObj("删除产品成功");
	}

	

	@RequestMapping("/product/list")
	@ResponseBody
	public PageVo<Product> listProduct(@Valid PageParam param, BindingResult result, Principal principal) {
		//
		if (principal == null) {
			JsonObj.newErrorJsonObj("用户已过期请从新登录");
		}
		int pageSize = param.getPageSize();
		int currentPage = param.getPageIndex();
		Groups g = new Groups();
		
		Page<Product> page = new Page<Product>(pageSize, currentPage);
		productService.ProductList(g, page);
		PageVo<Product> vo = new PageVo<>();
		// String picBasePath = values.getUploadedImagesUrlPrefix();
		vo.setTotal(page.getTotalCount());
		// vo.setPicPath(picBasePath);
		vo.setItems(page.getItems());
		return vo;
	}

	@RequestMapping("/product/loadProductById")
	@ResponseBody
	public JsonObj loadNewsById(Long productId, Principal principal) {
		//
		if (principal == null) {
			JsonObj.newErrorJsonObj("用户已过期请从新登录");
		}

		return JsonObj.newSuccessJsonObj("加载产品成功", productService.loadProductById(productId));
	}

}
