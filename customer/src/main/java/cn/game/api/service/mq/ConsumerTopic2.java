package cn.game.api.service.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
@Component
public class ConsumerTopic2 {
    @JmsListener(destination = "portal.admin.topic",containerFactory = "jmsListenerContainerTopic") // 监听指定消息主题
    public void receiveQueue(String text) {
       System.out.println("ConsumerTopic2="+text);
    }
}
