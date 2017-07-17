package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.UserGameResult;
import cn.game.core.repository.play.custom.UserGameResultRepositoryCustom;

@Repository
public interface UserGameResultRepository extends CrudRepository<UserGameResult, Long> , UserGameResultRepositoryCustom{

}
