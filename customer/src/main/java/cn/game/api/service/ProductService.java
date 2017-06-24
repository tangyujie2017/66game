package cn.game.api.service;

import cn.game.api.contorller.view.ProductVo;
import cn.tz.www.customer.entity.table.Product;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;

public interface ProductService {
	public ProductVo getProductById(Long productId,String imgUrl);
	public Page<ProductVo> loadProductList(Groups groups, Page<Product> page,String imgUrl);

}
