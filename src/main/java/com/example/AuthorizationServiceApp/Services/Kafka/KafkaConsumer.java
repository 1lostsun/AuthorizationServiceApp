package com.example.AuthorizationServiceApp.Services.Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

	@KafkaListener(topics = "aut-topic", groupId = "mail-group")
	public void listen(String message) {
		log.info(message);
	}

}
