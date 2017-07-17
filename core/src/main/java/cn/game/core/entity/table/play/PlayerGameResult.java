package cn.game.core.entity.table.play;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name="player_game_result")
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
	private Long resultTotalScore;//平台相对于这个客户赚？赔
	
	@Column
	private Integer type;//0：表示赚1：表示赔
	
	//游戏结果详情
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "gameresult_userresult")
	private List<UserGameResult> result;
}
