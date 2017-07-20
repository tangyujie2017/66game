package cn.game.api.service.mq;

import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@EnableScheduling
public class Producer  {
//	@Autowired
//    private JmsMessagingTemplate jmsMessagingTemplate;
   
	@Autowired
    private JmsTemplate  jmsTemplate ;
	
    @Autowired
    private Queue queue;
   
    @Autowired
    private Topic topic;

   //@Scheduled(fixedDelay=3000)//每3s执行1次
    public void send() {
      
//       this.jmsMessagingTemplate.convertAndSend(this.queue, "hi,activeMQ");
//     
//       this.jmsMessagingTemplate.convertAndSend(this.topic, "hi,activeMQ(topic)");
       this.jmsTemplate.convertAndSend(this.queue, new Email("唐军", "测试", "haha"));
       
      // this.jmsTemplate.convertAndSend(this.topic, "hi,activeMQ(topic)");
      
    }

}
