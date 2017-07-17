package cn.game.core.repository.play;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.game.core.entity.table.play.ResultAnimal;
import cn.game.core.repository.play.custom.ResultAnimalRepositoryCustom;

@Repository
public interface ResultAnimalRepository extends CrudRepository<ResultAnimal, Long> , ResultAnimalRepositoryCustom{

}
