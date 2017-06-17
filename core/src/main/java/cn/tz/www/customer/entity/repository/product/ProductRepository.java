package cn.tz.www.customer.entity.repository.product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.tz.www.customer.entity.table.Product;
import cn.tz.www.customer.entity.table.Slide;
import cn.tz.www.customer.entity.table.User;


@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, ProductRepositoryCustom {

    
    
}
