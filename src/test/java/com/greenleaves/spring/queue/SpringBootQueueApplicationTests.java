package com.greenleaves.spring.queue;

import org.apache.camel.*;
import org.apache.camel.test.spring.CamelSpringBootJUnit4ClassRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.*;
import javax.jms.Message;
import javax.xml.soap.Text;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
//@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class SpringBootQueueApplicationTests {

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    JmsTemplate jmsTemplate;

    @EndpointInject(uri = "activeMq:replyQueue")
    Endpoint replyQueueEndPoint;


    @Autowired
    ConsumerTemplate consumerTemplate;

    @Autowired
    ConnectionFactory connectionFactory;

    @Test
    @Ignore
    public void testCamelJMS_tempReplyQueue() {

        System.out.println(producerTemplate.requestBody("activeMq:sendQueue", "World"));
    }

    @Test
    public void testCamelJMS_setFixReplyQueue_withThread() throws Exception {


        Thread thread1 = new Thread(new SendMessageToQueue("msg1: "));
        Thread thread2 = new Thread(new SendMessageToQueue("msg2: "));
        Thread thread3 = new Thread(new SendMessageToQueue("msg3: "));
        thread1.start();
        thread2.start();
        thread3.start();

        while (true) {

        }

    }

    @Test
    public void testCamelJMS_fixReplyQueue() throws Exception {
        String result = producerTemplate.requestBody("activeMq:sendQueue?replyTo=replyQueueFoo", "Fix queue", String.class);
        System.out.println(result);

    }

    @Test
    public void testJmsTemplate_RequestReply() {
        final String correlationId = UUID.randomUUID().toString();
        jmsTemplate.send("sendQueue", session -> {
           TextMessage textMessage = session.createTextMessage("test");
            textMessage.setJMSCorrelationID(correlationId);
            return textMessage;
        });

        String replyMessage = (String) jmsTemplate.receiveSelectedAndConvert("replyQueue", "JMSCorrelationID='" + correlationId + "'");

        System.out.println(replyMessage);
    }

    @Test
    public void testSendJMS_Vanilla() throws Exception {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue sendQueue = session.createQueue("sendQueue");

        Queue replyQueue = session.createQueue("replyQueue");


        MessageProducer messageProducer = session.createProducer(sendQueue);

        connection.start();

        Message message = session.createTextMessage("Yo yo");

        message.setJMSReplyTo(replyQueue);
        String jmsCorrelationId = UUID.randomUUID().toString();
        message.setJMSCorrelationID(jmsCorrelationId);

        messageProducer.send(message);

//        MessageConsumer consumer = session.createConsumer(replyQueue, "JMSCorrelationID='" + jmsCorrelationId +"'");
//        TextMessage messageReply = (TextMessage) consumer.receive();
//        System.out.println(messageReply.getText());
        Thread.sleep(2000);

        //consumer.close();
        //connection.close();

    }

    class SendMessageToQueue implements Runnable {

        private String msg;

        public SendMessageToQueue() {
        }

        public SendMessageToQueue(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            while (true) {
                long timeMillis = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + " " + "========> Send: " + msg + " ===========================");
                String response = producerTemplate.requestBody("activeMq:sendQueue?replyTo=replyQueueFoo", msg, String.class);

                System.out.println(Thread.currentThread().getName() + " " + response);

                try {
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("OK");
                }

            }
        }
    }

}

