package com.crpdev.spring.boot.jms.sender;

import com.crpdev.spring.boot.jms.config.JmsConfig;
import com.crpdev.spring.boot.jms.model.HelloWorldMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        log.debug("<<< Sending a message >>>");
        HelloWorldMessage message = new HelloWorldMessage().builder()
                .id(UUID.randomUUID())
                .message("Hello World!")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.JMS_QUEUE, message);

        log.debug("<<< Message Sent >>>");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {
        log.debug("<<< sendAndReceiveMessage >>>");
        HelloWorldMessage message = new HelloWorldMessage().builder()
                .id(UUID.randomUUID())
                .message("Hello")
                .build();

        Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.JMS_SEND_RCV_QUEUE, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                Message msg = null;

                try {
                    msg = session.createTextMessage(objectMapper.writeValueAsString(message));
                    msg.setStringProperty("_type", "com.crpdev.spring.boot.jms.model.HelloWorldMessage");
                    log.debug("<<< Sending Hello >>>");
                    return msg;
                } catch (JsonProcessingException e) {
                    log.debug("Exception: ", e);
                    throw new JMSException("SOMETHING WRONG");
                }
            }
        });

        log.debug("<<< Received Message >>>");
        log.debug(receivedMsg.getBody(String.class));

    }

}
