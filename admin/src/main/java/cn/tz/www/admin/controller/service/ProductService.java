package cn.tz.www.admin.controller.service;

import cn.tz.www.customer.entity.table.Product;
import cn.tz.www.customer.entity.tools.Groups;
import cn.tz.www.customer.entity.tools.Page;

public interface ProductService {
	public Product loadProductById(Long id);
	public void saveProduct(Product product);
	public void deleteProduct(Long  product_id);
	public Page<Product> ProductList(Groups groups, Page<Product> page);
	
}
