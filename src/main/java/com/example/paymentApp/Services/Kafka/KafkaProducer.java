package com.example.paymentApp.Services.Kafka;

import com.example.paymentApp.Exceptions.MessageSendingException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

	public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(String topic, String key, String message) {
		kafkaTemplate.send(topic, key, message)
				.whenComplete((success, failure) -> {
					if (success != null) {
						log.info("Message sent to topic: {} key: {} message: {}", topic, key, message);
					} else {
						throw new MessageSendingException("Error sending message to topic: " + topic + ", key: " + key + ", message: " + message);
					}
				});
	}

}
