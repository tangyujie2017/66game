package cn.tz.www.customer.entity.repository.customerAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.tz.www.customer.entity.table.Authority;
import cn.tz.www.customer.entity.table.CustomerAuthority;
import cn.tz.www.customer.entity.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface CustomerAuthorityRepository extends CrudRepository<CustomerAuthority, Long>, CustomerAuthorityRepositoryCustom {
   
    
    
}
