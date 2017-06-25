package cn.game.api.service.logic;

import cn.game.core.tools.Page;

public interface GameLogicCenterService {
	//玩家选择后提交数据(提交到一个批次里)
	public void palyReady(Long userid,Object obj);
	//同一批次数据进行游戏规则匹配(如果足够10人否则系统用户进入)跑批任务
	public void matching();
	
	//用户查询所在批次的数据
	public Object getGameDataByBatch(Long userid,String batch);
	//查询用户所有游戏记录
	public Page getGameDataByBatchHistory(Long userid);
	

}
