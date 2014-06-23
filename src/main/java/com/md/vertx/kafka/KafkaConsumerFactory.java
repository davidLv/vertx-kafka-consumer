package com.md.vertx.kafka;

import com.google.inject.Inject;
import com.md.vertx.deserializer.Serializer;
import com.md.vertx.util.Configuration;

/**
 * User: mdyminski
 */
public class KafkaConsumerFactory {

    private final Serializer serializer;

    @Inject
    public KafkaConsumerFactory(Serializer serializer) {
        this.serializer = serializer;
    }

    public KafkaConsumer createConsumer(KafkaMessageReceiver receiver, Configuration config) {
        return new KafkaConsumer(receiver, config, serializer);
    }
}
