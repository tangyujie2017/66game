package cn.game.core.repository.system;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.system.Authority;
import cn.game.core.repository.system.custom.AuthorityRepositoryCustom;



/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long>, AuthorityRepositoryCustom {
   
    
    
}
