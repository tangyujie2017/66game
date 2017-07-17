package cn.game.core.entity.table.play;

import java.io.Serializable;
import java.util.Date;
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

@Entity(name = "player_game")
public class PlayerGame implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id")
	private Player player;
	
	@Column(name = "batch_num")
	private String batchNum;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "total_score")
	private Long totalScore;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "playegame_gameanimal")
	private List<GameAnimal> animalList;

	

}
