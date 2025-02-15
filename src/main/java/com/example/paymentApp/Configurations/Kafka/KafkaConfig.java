package com.example.paymentApp.Configurations.Kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String autoOffsetReset;

	@Bean
	public ProducerFactory<String, String> producerFactory() {

		Map<String, Object> configProperties = new HashMap<>();
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return new DefaultKafkaProducerFactory<>(configProperties);
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> configProperties = new HashMap<>();

		configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(configProperties);

	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
