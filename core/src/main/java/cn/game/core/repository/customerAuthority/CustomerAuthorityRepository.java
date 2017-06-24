package cn.game.core.repository.customerAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.table.Authority;
import cn.game.core.table.CustomerAuthority;
import cn.game.core.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface CustomerAuthorityRepository extends CrudRepository<CustomerAuthority, Long>, CustomerAuthorityRepositoryCustom {
   
    
    
}
