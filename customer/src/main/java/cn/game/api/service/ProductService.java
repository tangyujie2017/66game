package cn.game.api.service;

import cn.game.api.contorller.view.ProductVo;
import cn.game.core.table.Product;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

public interface ProductService {
	public ProductVo getProductById(Long productId,String imgUrl);
	public Page<ProductVo> loadProductList(Groups groups, Page<Product> page,String imgUrl);

}
