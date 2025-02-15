package com.example.paymentApp.Configurations.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic paymentTopic() {
		return new NewTopic("auth-topic", 1, (short) 1);
	}

}
