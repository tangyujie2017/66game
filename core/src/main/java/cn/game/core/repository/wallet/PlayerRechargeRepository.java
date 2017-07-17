package cn.game.core.repository.wallet;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.wallet.PlayerRecharge;
import cn.game.core.repository.wallet.custom.PlayerRechargeRepositoryCustom;
@Repository
public interface PlayerRechargeRepository extends CrudRepository<PlayerRecharge, Long> , PlayerRechargeRepositoryCustom{

}
