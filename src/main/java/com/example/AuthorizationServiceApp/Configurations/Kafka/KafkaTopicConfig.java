package com.example.AuthorizationServiceApp.Configurations.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic authTopic() {
		return new NewTopic("auth-topic", 3, (short) 1);
	}

}
