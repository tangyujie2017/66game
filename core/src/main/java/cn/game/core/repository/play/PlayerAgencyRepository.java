package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.PlayerAgency;
import cn.game.core.repository.play.custom.PlayerAgencyRepositoryCustom;

@Repository
public interface PlayerAgencyRepository extends CrudRepository<PlayerAgency, Long> , PlayerAgencyRepositoryCustom{

}
