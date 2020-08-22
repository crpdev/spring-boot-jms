package com.crpdev.spring.boot.jms.listener;

import com.crpdev.spring.boot.jms.config.JmsConfig;
import com.crpdev.spring.boot.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloWorldListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.JMS_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage, @Headers MessageHeaders headers, Message message){
    log.debug("<<< Received Message >>>");
    log.debug(helloWorldMessage.toString());

    }

//    @JmsListener(destination = JmsConfig.JMS_SEND_RCV_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage,
                               @Headers MessageHeaders headers, Message message) throws JMSException {

        log.debug("<<< Received Message on Send And Receive Queue >>>");
        log.debug(helloWorldMessage.toString());

        HelloWorldMessage payloadMsg = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("World!!")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payloadMsg);

    }
}
