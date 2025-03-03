package com.example.AuthorizationServiceApp.Services.Kafka;

import com.example.AuthorizationServiceApp.Dto.MessageDto;
import com.example.AuthorizationServiceApp.Exceptions.MessageSendingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

	private final ObjectMapper objectMapper;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

	public KafkaProducer(ObjectMapper objectMapper, KafkaTemplate<String, Object> kafkaTemplate) {
		this.objectMapper = objectMapper;
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(String topic, String key, MessageDto message) throws JsonProcessingException {
		String jsonMessage = objectMapper.writeValueAsString(message);
		kafkaTemplate.send(topic, key, jsonMessage)
				.whenComplete((success, failure) -> {
					if (success != null) {
						log.info("Message sent to topic: {} key: {} message: {}", topic, key, message);
					} else {
						throw new MessageSendingException("Error sending message to topic: " + topic + ", key: " + key + ", message: " + message);
					}
				});
	}

}
