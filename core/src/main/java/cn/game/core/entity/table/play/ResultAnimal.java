package cn.game.core.entity.table.play;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="result_animal_detail")
public class ResultAnimal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY )
	@JoinColumn(name = "animal_id")
	private BaseAnimal animal;
	@Column
	private Integer originalScore;
	
	@Column
	private Integer resultScore;
	@Column
	private boolean selected=false;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BaseAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(BaseAnimal animal) {
		this.animal = animal;
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
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	
	
}
