package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.PlayerAccount;
import cn.game.core.repository.play.custom.PlayerAccountRepositoryCustom;
@Repository
public interface PlayerAccountRepository extends CrudRepository<PlayerAccount, Long> , PlayerAccountRepositoryCustom{

}
