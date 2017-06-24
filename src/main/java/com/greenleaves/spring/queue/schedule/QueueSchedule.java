package com.greenleaves.spring.queue.schedule;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by Nguyen Duy Tiep on 24-Jun-17.
 */
@Component
public class QueueSchedule {

    @Autowired
    ProducerTemplate producerTemplate;

    //@Scheduled(fixedRate = 1L)
    public void sendRandomMessageToQueue() {
        producerTemplate.send("activeMq:sendQueue?preserveMessageQos=true", exchange -> {
            exchange.getIn().setBody("Msg: " + UUID.randomUUID());
            exchange.getIn().setHeader("JMSReplyTo", "replyQueue");
            exchange.getIn().setHeader("JMSCorrelationID", UUID.randomUUID());
        });
    }
}
