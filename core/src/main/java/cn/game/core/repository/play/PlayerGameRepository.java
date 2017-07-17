package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.PlayerGame;
import cn.game.core.repository.play.custom.PlayerGameRepositoryCustom;

@Repository
public interface PlayerGameRepository extends CrudRepository<PlayerGame, Long>, PlayerGameRepositoryCustom {

}
