package cn.game.api.service.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import cn.game.core.entity.table.play.PlayerGameResult;

@Component
public class ConsumerTopic1 {
    @JmsListener(destination = "portal.admin.topic",containerFactory = "jmsListenerContainerTopic") // 监听指定消息主题
    public void receiveQueue(PlayerGameResult result) {
       System.out.println("ConsumerTopic1="+ result);
    }
}
