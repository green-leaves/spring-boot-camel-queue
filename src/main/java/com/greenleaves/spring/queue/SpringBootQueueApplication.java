package com.greenleaves.spring.queue;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableScheduling
public class SpringBootQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootQueueApplication.class, args);
	}

    @Bean
    CamelContextConfiguration camelContextConfiguration(ActiveMQComponent activeMQComponent) {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                  camelContext.addComponent("activeMq", activeMQComponent);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {

            }
        };
    }

    @Bean
	ActiveMQComponent activeMQComponent(ConnectionFactory connectionFactory) {
        ActiveMQComponent component = new ActiveMQComponent();
        component.setConnectionFactory(connectionFactory);
        return component;
    }
}
