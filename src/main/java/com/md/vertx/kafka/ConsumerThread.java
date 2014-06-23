package com.md.vertx.kafka;

import com.md.vertx.deserializer.Serializer;
import com.md.vertx.deserializer.SerializerException;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: mdyminski
 */
class ConsumerThread<T> implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerThread.class);

    private final KafkaMessageReceiver<T> messageReceiver;

    private final KafkaStream<byte[], byte[]> stream;

    private final Serializer<T> kafkaDeserializer;

    public ConsumerThread(KafkaMessageReceiver<T> messageReceiver, KafkaStream<byte[], byte[]> stream, Serializer<T> kafkaDeserializer) {
        this.stream = stream;
        this.messageReceiver = messageReceiver;
        this.kafkaDeserializer = kafkaDeserializer;
    }

    @Override
    public void run() {
        getMessageAndSendToReceiver();
    }

    private void getMessageAndSendToReceiver() {
        while (stream.iterator().hasNext()) {
            MessageAndMetadata<byte[], byte[]> msg = stream.iterator().next();
            try {
                T decodedObject = kafkaDeserializer.deserialize(msg.message());
                messageReceiver.receive(decodedObject);
            } catch (SerializerException e) {
                LOGGER.error("Error cannot decode object received from kafka!", e);
            }
        }
    }

}
