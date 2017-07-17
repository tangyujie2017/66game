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
	//游戏结果详情
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id")
	private Player player;
	
	private Date createDate;
	
	private Long rechargeAmount;
	
	private Long rechargeScore;
	
	private Integer rechargeSource;
	
	private Integer operateType;;
	

}
