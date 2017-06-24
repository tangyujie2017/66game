package cn.game.core.repository.user;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.table.User;

/**
 * Created by zzc on 11/11/2016.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>, UserRepositoryCustom {
    public User findByMobile(String mobile);
    
    
}
