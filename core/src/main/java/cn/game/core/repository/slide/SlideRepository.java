package cn.game.core.repository.slide;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.table.Slide;
import cn.game.core.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface SlideRepository extends CrudRepository<Slide, Long>, SlideRepositoryCustom {

    
    
}
