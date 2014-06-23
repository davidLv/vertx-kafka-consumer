package com.md.vertx.kafka;

import com.google.common.collect.ImmutableMap;
import com.md.vertx.deserializer.Serializer;
import com.md.vertx.util.Configuration;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: mdyminski
 */
public class KafkaConsumer<T> {

	/* Currently we define only one consuming thread */
    private static final int THREADS = 1;

    protected static final int ZK_CONNECTION_TIMEOUT_MS = 100000;

    private final List<KafkaStream<byte[], byte[]>> kafkaStreams;

    private final ExecutorService executor;

    private final KafkaMessageReceiver<T> messageReceiver;

    private final Serializer<T> deserializer;

    public KafkaConsumer(KafkaMessageReceiver<T> messageReceiver, Configuration config,
                         Serializer<T> deserializer) {
        this(messageReceiver, createProperties(config.getZkConnect(), config.getGroupId(), ZK_CONNECTION_TIMEOUT_MS), config.getKafkaTopic(), deserializer);
    }

    public KafkaConsumer(KafkaMessageReceiver<T> messageReceiver, Properties props, String topic, Serializer<T> deserializer) {
        this(messageReceiver, getKafkaStreams(topic, props), deserializer, getExecutor());
    }

    public KafkaConsumer(KafkaMessageReceiver<T> messageReceiver, List<KafkaStream<byte[], byte[]>> kafkaStreams,
                         Serializer<T> deserializer, ExecutorService executor) {
        this.messageReceiver = messageReceiver;
        this.kafkaStreams = kafkaStreams;
        this.deserializer = deserializer;
        this.executor = executor;
    }

    public void consume() {
        for (final KafkaStream<byte[], byte[]> stream : this.kafkaStreams) {
            this.executor.execute(getConsumerThread(this.messageReceiver, stream));
        }
    }

    protected ConsumerThread<T> getConsumerThread(KafkaMessageReceiver<T> messageReceiver, KafkaStream<byte[], byte[]> stream) {
        return new ConsumerThread(messageReceiver, stream, deserializer);
    }

    private static List<KafkaStream<byte[], byte[]>> getKafkaStreams(String topic, Properties props) {
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams = consumerConnector
                .createMessageStreams(ImmutableMap.of(topic, THREADS));
        return topicMessageStreams.get(topic);
    }

    private static ExecutorService getExecutor() {
        return Executors.newFixedThreadPool(THREADS);
    }

    protected static Properties createProperties(String zkHost, String groupId, int zookeeperTimeoutInMs) {
        // Create the connection to the cluster
        Properties props = new Properties();

        props.setProperty("zookeeper.connect", zkHost);
        props.setProperty("group.id", groupId);
        props.setProperty("zookeeper.connection.timeout.ms", Integer.toString(zookeeperTimeoutInMs));

        return props;
    }
}
