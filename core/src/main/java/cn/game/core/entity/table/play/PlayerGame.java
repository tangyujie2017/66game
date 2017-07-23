package cn.game.core.entity.table.play;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
	
	@ManyToMany(fetch = FetchType.LAZY  ,cascade=CascadeType.ALL )
	@JoinTable(name = "playegame_gameanimal")
	private List<GameAnimal> animalList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	public List<GameAnimal> getAnimalList() {
		return animalList;
	}

	public void setAnimalList(List<GameAnimal> animalList) {
		this.animalList = animalList;
	}

	

}
