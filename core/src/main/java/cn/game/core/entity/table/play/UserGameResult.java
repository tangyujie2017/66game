package cn.game.core.entity.table.play;

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
@Entity
public class UserGameResult {
  
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id")
	private Player player;
	//游戏结果详情
	@ManyToMany(fetch = FetchType.LAZY ,cascade =CascadeType.ALL)
	@JoinTable(name = "userresult_resultanimal")
	private List<ResultAnimal> details;
	
	
	@Column
	private Integer originalScore;
	
	@Column
	private Integer resultScore;

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

	public List<ResultAnimal> getDetails() {
		return details;
	}

	public void setDetails(List<ResultAnimal> details) {
		this.details = details;
	}

	public Integer getOriginalScore() {
		return originalScore;
	}

	public void setOriginalScore(Integer originalScore) {
		this.originalScore = originalScore;
	}

	public Integer getResultScore() {
		return resultScore;
	}

	public void setResultScore(Integer resultScore) {
		this.resultScore = resultScore;
	}
	
	
}
