package cn.game.core.entity.table.play;


//
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
@Entity(name = "player_account")
public class PlayerAccount implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue
	@Column(name = "accout_id")
	private Long accout_id;
	
	@Column(name = "total_gold")
	private  Long totalGold=0l;
	
	@OneToOne(mappedBy="accout")  
	private Player player;
	
    @Column(name="history_pay_record")
	private Long historyPayRecord;//历史总充值记录
    
	public Long getAccout_id() {
		return accout_id;
	}

	public void setAccout_id(Long accout_id) {
		this.accout_id = accout_id;
	}

	public Long getTotalGold() {
		return totalGold;
	}

	public void setTotalGold(Long totalGold) {
		this.totalGold = totalGold;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Long getHistoryPayRecord() {
		return historyPayRecord;
	}

	public void setHistoryPayRecord(Long historyPayRecord) {
		this.historyPayRecord = historyPayRecord;
	}
	
	

}
