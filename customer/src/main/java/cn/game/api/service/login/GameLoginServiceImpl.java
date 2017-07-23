package cn.game.api.service.login;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.game.core.entity.table.play.Player;
import cn.game.core.repository.play.PlayerRepository;

@Service
public class GameLoginServiceImpl implements GameLoginService {
 @Autowired
 private PlayerRepository playerRepository;
 
	@Override
	public Player checkWxUser(String wxUnionid) {
		return playerRepository.findUniqueBy("wxUnionid", wxUnionid);
	}

	@Override
	@Transactional
	public Player saveWxUser(Player player) {
		player=playerRepository.save(player);
		return player;
	}

	@Override
	@Transactional
	public Player updateWxUser(Player player) {
		playerRepository.update(player);
		return player;
	}

	

}
