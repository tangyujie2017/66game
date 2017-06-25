package cn.game.core.repository.system;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.system.CustomerAuthority;
import cn.game.core.repository.system.custom.CustomerAuthorityRepositoryCustom;
@Repository
public interface CustomerAuthorityRepository extends CrudRepository<CustomerAuthority, Long>, CustomerAuthorityRepositoryCustom {
   
    
    
}
