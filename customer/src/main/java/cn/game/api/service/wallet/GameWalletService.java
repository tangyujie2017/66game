package cn.game.api.service.wallet;

import java.math.BigDecimal;

public interface GameWalletService {
//充值
public boolean recharge(Long userid,BigDecimal money);
//赠送
public boolean giveAway(Long giveAwayUserid,Long  receptor,Long score );
//充值日志
void rechargeLog() ;
//赠送日志
void giveAwayLog();
}
