package com.greenleaves.spring.queue.service;

import org.apache.camel.Consume;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Nguyen Duy Tiep on 03-Jun-17.
 */
@Component
public class MessageHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    public void handleMassage(String body, Exchange exchange) {
        logger.info("Body: " + body);
        for (Map.Entry entry : exchange.getIn().getHeaders().entrySet()) {
            logger.info(entry.getKey() + ": " + entry.getValue());
        }
        exchange.getOut().setBody("========> Reply " + body + " ===========================");
    }

}
