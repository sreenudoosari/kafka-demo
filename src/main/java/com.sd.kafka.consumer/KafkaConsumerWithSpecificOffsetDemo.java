package com.sd.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerWithSpecificOffsetDemo {

    public static void main(String[] args) {
        Properties props = getProperties();
        String topicName = "test-topic-part2";
        int partitionNumber = 1; // choose your partition
        long offsetToStart = 7L; // choose your offset

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            TopicPartition partition = new TopicPartition(topicName, partitionNumber);

            // Assign specific partition
            consumer.assign(Collections.singletonList(partition));

            // Seek to specific offset
            consumer.seek(partition, offsetToStart);

            System.out.println("Consuming from " + topicName + " starting at offset " + offsetToStart);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Received message: key=%s, value=%s, partition=%d, offset=%d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                }
            }
        }
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-offset-test");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // AUTO_OFFSET_RESET is ignored when using assign/seek
        return props;
    }
}