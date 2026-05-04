package com.example.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration // Marks this class as Spring configuration (defines beans)
@EnableKafka // Enables Kafka listener support (@KafkaListener)
public class KafkaConfig {

	// PRODUCER CONFIG
	@Bean
	public ProducerFactory<String, Object> producerFactory() {

		// Configuration map for Kafka Producer
		Map<String, Object> config = new HashMap<>();

		// Address of Kafka server (broker)
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

		// Serializer for message key (String → bytes)
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// Serializer for message value (Java Object → JSON → bytes)
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		// Create ProducerFactory using above configs
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {

		// KafkaTemplate is used to send messages to Kafka topics
		// Example: kafkaTemplate.send("topic-name", object);
		return new KafkaTemplate<>(producerFactory());
	}


	// CONSUMER CONFIG

	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {

		// Deserializer to convert JSON → Java Object
		JsonDeserializer<Object> deserializer = new JsonDeserializer<>();

		// Trust all packages (⚠️ safe for dev, restrict in production)
		deserializer.addTrustedPackages("*");

		// Configuration map for Kafka Consumer
		Map<String, Object> props = new HashMap<>();

		// Kafka server address
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

		// Consumer group ID (used for load balancing and message sharing)
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "booking-group");

		// Deserializer for key (bytes → String)
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		// Deserializer for value (bytes → JSON → Java Object)
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

		// Create ConsumerFactory using configs and deserializers
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
	}

	// LISTENER CONTAINER

	@Bean(name = "kafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {

		// Factory that manages Kafka listeners (@KafkaListener)
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();

		// Set the consumer factory (how messages are read)
		factory.setConsumerFactory(consumerFactory());

		// Now Spring knows how to consume messages using this config
		return factory;
	}
}