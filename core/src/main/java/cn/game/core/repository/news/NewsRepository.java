package cn.game.core.repository.news;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.table.News;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface NewsRepository extends CrudRepository<News, Long>, NewsRepositoryCustom {
   
    
    
}
