package cn.game.core.repository.product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.table.Product;
import cn.game.core.table.Slide;
import cn.game.core.table.User;


@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, ProductRepositoryCustom {

    
    
}
