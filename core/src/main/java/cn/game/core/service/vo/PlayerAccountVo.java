package cn.game.core.service.vo;



import cn.game.core.entity.table.play.Player;

public class PlayerAccountVo {

	
	
	private Long accout_id;
	private  Long totalGold=0l;
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


	public Long getHistoryPayRecord() {
		return historyPayRecord;
	}

	public void setHistoryPayRecord(Long historyPayRecord) {
		this.historyPayRecord = historyPayRecord;
	}
	
	

}
