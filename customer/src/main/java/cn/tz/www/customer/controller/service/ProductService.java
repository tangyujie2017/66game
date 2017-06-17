package cn.tz.www.customer.controller.service;

import cn.tz.www.customer.entity.table.Product;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;
import cn.tz.www.customer.view.ProductVo;

public interface ProductService {
	public ProductVo getProductById(Long productId,String imgUrl);
	public Page<ProductVo> loadProductList(Groups groups, Page<Product> page,String imgUrl);

}
