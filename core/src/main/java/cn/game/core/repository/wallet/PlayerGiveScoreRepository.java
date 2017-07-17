package cn.game.core.repository.wallet;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.wallet.PlayerGiveScore;
import cn.game.core.repository.wallet.custom.PlayerGiveScoreRepositoryCustom;

@Repository
public interface PlayerGiveScoreRepository extends CrudRepository<PlayerGiveScore, Long> , PlayerGiveScoreRepositoryCustom{

}
