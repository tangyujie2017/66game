package cn.tz.www.customer.entity.repository.news;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.tz.www.customer.entity.table.News;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface NewsRepository extends CrudRepository<News, Long>, NewsRepositoryCustom {
   
    
    
}
