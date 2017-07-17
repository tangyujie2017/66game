package cn.game.core.repository.wallet;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.wallet.PlayerWithDraw;
import cn.game.core.repository.wallet.custom.PlayerWithDrawRepositoryCustom;

@Repository
public interface PlayerWithDrawRepository extends CrudRepository<PlayerWithDraw, Long> , PlayerWithDrawRepositoryCustom{

}
