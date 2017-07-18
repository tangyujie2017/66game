package cn.game.core.entity.table.play;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "game_animal")
public class GameAnimal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "animal_id")
	private BaseAnimal animal;
	//
	@Column
	private Integer score;
	//赔分
	@Column(name="total_score")
	private  Long totalScore;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	public BaseAnimal getAnimal() {
		return animal;
	}

	public void setAnimal(BaseAnimal animal) {
		this.animal = animal;
	}
	
	

}
