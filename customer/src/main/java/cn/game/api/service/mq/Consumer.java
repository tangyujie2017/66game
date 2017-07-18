package cn.game.api.service.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	@JmsListener(destination = "portal.admin.queue",containerFactory = "jmsListenerContainerQueue") // 监听指定消息主题
	    public void receiveQueue(Email text) {
	       System.out.println(text);
	    }
}
