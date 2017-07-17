package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.BaseAnimal;
import cn.game.core.repository.play.custom.BaseAnimalRepositoryCustom;
@Repository
public interface BaseAnimalRepository extends CrudRepository<BaseAnimal, Long> , BaseAnimalRepositoryCustom{

}
