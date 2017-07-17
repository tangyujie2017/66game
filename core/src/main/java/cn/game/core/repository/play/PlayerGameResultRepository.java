package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.PlayerGameResult;
import cn.game.core.repository.play.custom.PlayerGameResultRepositoryCustom;

@Repository
public interface PlayerGameResultRepository extends CrudRepository<PlayerGameResult, Long> , PlayerGameResultRepositoryCustom{

}
