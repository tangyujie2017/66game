package cn.game.api.service.login;

import cn.game.core.entity.table.play.Player;

public interface GameLoginService {
	public Player  checkWxUser(String wxUnionid);
	public Player  saveWxUser(Player player);
	public Player  updateWxUser(Player player);

}
