package cn.game.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.game.api.service.ProductService;
import cn.game.core.table.Product;
import cn.game.core.tools.Groups;
import cn.game.core.tools.JsonObj;
import cn.game.core.tools.Page;

@Controller
public class CustomerProductController {
	@Autowired
	private ProductService productService;
	@Value("${customer.images.url-prefix}")
	private String imgUrl;
	// 注册
	@GetMapping(value = "/api/customer/product/list")
	@ResponseBody
	public JsonObj loadProductList( Integer pageSize,Integer currentPage) {
		if (pageSize!=null&&currentPage!=null) {
           Groups g = new Groups();
			
			Page<Product> page = new Page<Product>(pageSize, currentPage);
			return JsonObj.newSuccessJsonObj("获取消息成功", productService.loadProductList(g, page, imgUrl));
			
		}else{
			return JsonObj.newErrorJsonObj("请求参数不正确");
		}
	
		
		

	}

	

	@GetMapping(value = "/api/customer/product/loadProductById")
	@ResponseBody
	public JsonObj getProductById(Long productId) {
		if (productId == null) {
			return JsonObj.newErrorJsonObj("请求参数不正确");
		}

		return JsonObj.newSuccessJsonObj("获取数据成功", productService.getProductById(productId, imgUrl));

	}
}
