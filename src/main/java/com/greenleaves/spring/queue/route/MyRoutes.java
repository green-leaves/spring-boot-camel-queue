package com.greenleaves.spring.queue.route;

import com.greenleaves.spring.queue.service.MessageHandler;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by Nguyen Duy Tiep on 03-Jun-17.
 */
@Component
public class MyRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start").to("activeMq:sendQueue");

        from("activeMq:sendQueue").bean(MessageHandler.class, "handleMassage");

        //from("activeMq:replyQueue").log("${body}");

    }
}
