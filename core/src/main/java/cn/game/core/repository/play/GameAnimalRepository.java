package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.GameAnimal;
import cn.game.core.repository.play.custom.GameAnimalRepositoryCustom;

@Repository
public interface GameAnimalRepository extends CrudRepository<GameAnimal, Long> , GameAnimalRepositoryCustom{

}
