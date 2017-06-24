package cn.game.admin.controller.service;

import cn.game.core.table.Product;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;

public interface ProductService {
	public Product loadProductById(Long id);
	public void saveProduct(Product product);
	public void deleteProduct(Long  product_id);
	public Page<Product> ProductList(Groups groups, Page<Product> page);
	
}
