package cn.tz.www.customer.entity.repository.slide;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.tz.www.customer.entity.table.Slide;
import cn.tz.www.customer.entity.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface SlideRepository extends CrudRepository<Slide, Long>, SlideRepositoryCustom {

    
    
}
