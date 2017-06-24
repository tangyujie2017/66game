package cn.game.core.repository.authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.table.Authority;
import cn.game.core.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long>, AuthorityRepositoryCustom {
   
    
    
}
