package cn.game.admin.controller.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.game.admin.controller.service.ProductService;
import cn.game.core.repository.product.ProductRepository;
import cn.game.core.table.Product;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;
@Service
public class ProductServiceImpl implements ProductService{
@ Autowired
private ProductRepository productRepository;

@Transactional
public void saveProduct(Product product){
	
	productRepository.persist(product);
}
@Transactional
public void deleteProduct(Long  product_id){
	productRepository.delete(product_id);
}
public Page<Product> ProductList(Groups groups, Page<Product> page) {

	return productRepository.findEntityPageByGroups(groups, page);

}

public Product loadProductById(Long id) {
	return productRepository.find(id);
}
}
