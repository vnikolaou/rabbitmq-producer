package com.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {
    @Autowired
    private ObjectFactory<RabbitTemplate> rabbitTemplateClientFactory;	  		 

	public void send(String input) throws IOException {
		RabbitTemplate rabbitTemplate = rabbitTemplateClientFactory.getObject();

		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("message", input);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try(ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
			out.writeObject(payload);   	
		}

		String correlationID = new BigInteger(130, new SecureRandom()).toString(32);
		
		System.out.println("Started at: " + System.currentTimeMillis());
	
		for(int i=0;i<1000;i++) {
			Message message = MessageBuilder
					.withBody(byteOut.toByteArray())
					.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
					.setCorrelationId(correlationID.getBytes()).build();
			rabbitTemplate.send("test-exchange", "test-key", message);	  	
		}
		
		System.out.println("Messages are sent: " + input + " (correlationID: " + correlationID + ")");
	}
}
