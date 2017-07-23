package cn.game.api.service.logic;

import java.util.List;

import cn.game.api.controller.resp.logic.GameResultResp;
import cn.game.api.service.arithmetic.Animal;
import cn.game.core.entity.table.play.PlayerGame;
import cn.game.core.entity.table.play.PlayerGameResult;
import cn.game.core.entity.table.play.UserGameResult;
import cn.game.core.tools.Page;

public interface GameLogicCenterService {
	//玩家选择后提交数据(提交到一个批次里)
	public  String receiver( Long userId, List<Animal> list) throws Exception ;
	
	
	
	
	
	public  void matcher(String batch);
	
	public GameResultResp loadUserPlayResult(String currentBatch, Long userId);
	
	

}
