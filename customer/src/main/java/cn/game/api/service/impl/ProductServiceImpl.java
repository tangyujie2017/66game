package cn.game.api.service.impl;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.game.api.contorller.view.ProductVo;
import cn.game.api.service.ProductService;
import cn.game.core.repository.product.ProductRepository;
import cn.game.core.table.Product;
import cn.game.core.tools.Groups;
import cn.game.core.tools.Page;
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Page<ProductVo> loadProductList(Groups groups, Page<Product> page,String imgUrl) {
		
		page  = productRepository.findEntityPageByGroups(groups, page);
		
		
		return convertProduct(page,imgUrl);
	}

	@SuppressWarnings("unchecked")
	private Page<ProductVo> convertProduct(Page<Product> page,String imgUrl) {
		Page<ProductVo> result=new Page<>(page.getPageSize(), page.getCurrentPage());
		result.setFromIndex(page.getFromIndex());
		result.setTotalCount(page.getTotalCount());
		result.setTotalPageCount(page.getTotalPageCount());
		List<ProductVo> vos = new ArrayList<ProductVo>();
		page.getItems().stream().forEach(a -> {
			Product p=(Product)a;
			ProductVo vo = new ProductVo();
			vo.setId(p.getId());
			vo.setName(p.getName());
			vo.setProductDesc(p.getProduct_desc());
			vo.setProductDetail(p.getProduct_detail());
			vo.setProductImg(p.getProduct_img());
			vo.setBaseUrl(imgUrl);
		
			vos.add(vo);
		});
		result.setItems(vos);
		return  result;
	}

	

	@Override
	public ProductVo getProductById(Long productId,String imgUrl) {
		Product p = productRepository.find(productId);
		ProductVo vo = new ProductVo();
		vo.setId(p.getId());
		vo.setName(p.getName());
		vo.setProductDesc(p.getProduct_desc());
		vo.setProductDetail(p.getProduct_detail());
		vo.setProductImg(p.getProduct_img());
		vo.setBaseUrl(imgUrl);
		return vo;
	}

}
