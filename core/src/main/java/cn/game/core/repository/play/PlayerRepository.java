package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.Player;
import cn.game.core.repository.play.custom.PlayerRepositoryCustom;
@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> , PlayerRepositoryCustom{

}
