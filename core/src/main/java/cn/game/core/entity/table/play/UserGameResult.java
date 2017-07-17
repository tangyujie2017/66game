package cn.game.core.entity.table.play;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
@Entity(name="user_game_result")
public class UserGameResult {
  
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id")
	private Player player;
	//游戏结果详情
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "userresult_resultanimal")
	private List<ResultAnimal> details;
	
	@Column
	private Integer originalScore;
	
	@Column
	private Integer resultScore;
}
