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

@Entity
public class PlayerGameResult implements Serializable{
	
	private static final long serialVersionUID = -4523597277367945688L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	
	
	@Column(name = "batch_num")
	private String batchNum;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "income_total_score")
	private Long incomeTotalScore;//进入数据
	
	@Column(name = "out_total_score")
	private Long outTotalScore;
	
	@Column(name = "result_total_score")
	private Long resultTotalScore;//平台相对于这个批次赚？赔
	
	@Column
	private Integer type;//0：表示赚1：表示赔
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "animal_id")
	private BaseAnimal animal;
	
	public BaseAnimal getAnimal() {
		return animal;
	}

	public void setAnimal(BaseAnimal animal) {
		this.animal = animal;
	}

	//游戏结果详情
	@ManyToMany(fetch = FetchType.LAZY ,cascade=CascadeType.ALL)
	@JoinTable(name = "gameresult_userresult")
	private List<UserGameResult> result;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getIncomeTotalScore() {
		return incomeTotalScore;
	}

	public void setIncomeTotalScore(Long incomeTotalScore) {
		this.incomeTotalScore = incomeTotalScore;
	}

	public Long getOutTotalScore() {
		return outTotalScore;
	}

	public void setOutTotalScore(Long outTotalScore) {
		this.outTotalScore = outTotalScore;
	}

	public Long getResultTotalScore() {
		return resultTotalScore;
	}

	public void setResultTotalScore(Long resultTotalScore) {
		this.resultTotalScore = resultTotalScore;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<UserGameResult> getResult() {
		return result;
	}

	public void setResult(List<UserGameResult> result) {
		this.result = result;
	}
	
	
	
}
