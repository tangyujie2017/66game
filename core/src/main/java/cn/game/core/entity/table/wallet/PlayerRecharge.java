package cn.game.core.entity.table.wallet;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.game.core.entity.table.play.Player;

//用户充值相关
@Entity(name = "player_recharge")
public class PlayerRecharge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4422989369572483940L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id")
	private Player player;
	@Column
	private Date createDate;
	@Column
	private Long rechargeAmount;
	@Column
	private Long rechargeScore;
	//充值来源
	@Column
	private Integer rechargeSource;//1：api手机端，2后台管理充值 3其他
	//处理类型
	@Column
	private Integer operateType;//1新建，2完成，3无效，4过期
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(Long rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public Long getRechargeScore() {
		return rechargeScore;
	}
	public void setRechargeScore(Long rechargeScore) {
		this.rechargeScore = rechargeScore;
	}
	public Integer getRechargeSource() {
		return rechargeSource;
	}
	public void setRechargeSource(Integer rechargeSource) {
		this.rechargeSource = rechargeSource;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	
	
	
	
	
	

}
