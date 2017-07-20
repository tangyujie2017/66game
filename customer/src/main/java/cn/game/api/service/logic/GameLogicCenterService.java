package cn.game.api.service.logic;

import java.util.List;
import cn.game.api.service.arithmetic.Animal;
import cn.game.core.entity.table.play.PlayerGame;
import cn.game.core.entity.table.play.PlayerGameResult;
import cn.game.core.tools.Page;

public interface GameLogicCenterService {
	//玩家选择后提交数据(提交到一个批次里)
	public String palyReady(Long userid,List<Animal> list) throws Exception;
	
	
	// 保存用户选择数据
	public void savePalyData(PlayerGame playerGame) throws Exception;
	
	
	public void saveResultData(PlayerGameResult playerGameResult) throws Exception ;
	
	

}
