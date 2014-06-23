package com.md.vertx;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.md.vertx.deserializer.StringSerializer;
import com.md.vertx.kafka.KafkaConsumer;
import com.md.vertx.kafka.KafkaConsumerFactory;
import com.md.vertx.kafka.KafkaMessageReceiver;
import com.md.vertx.util.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;

/**
 * User: mdyminski
 */
public class KafkaMessageConsumer extends BusModBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    public void start() {
//        super.start();
        LOGGER.info("Starting kafka consumer module!");
        Configuration config = prepareConfig();

        KafkaConsumer kafkaConsumer = prepareKafkaConsumer(config);

        kafkaConsumer.consume();

        LOGGER.info("Kafka consumer module started!");
    }

    @VisibleForTesting
    protected KafkaConsumer prepareKafkaConsumer(Configuration config) {
        // it cannot be in constructor due to fact that start() is main method in vertx
        Injector injector = Guice.createInjector(new VertxKafkaConsumerModule(config));
        KafkaConsumerFactory kafkaConsumerFactory = injector.getInstance(KafkaConsumerFactory.class);

        KafkaMessageReceiver<String> receiver = getReceiver(config.getVertxTopic(), vertx.eventBus());
        KafkaConsumer kafkaConsumer = kafkaConsumerFactory.createConsumer(receiver, config);

        return kafkaConsumer;
    }

    @VisibleForTesting
    protected Configuration prepareConfig() {
        return Configuration.builder().groupId(getMandatoryStringConfig("groupId"))
                .kafkaTopic(getMandatoryStringConfig("kafkaTopic"))
                .vertxTopic(getMandatoryStringConfig("vertxTopic"))
                .zkConnect(getMandatoryStringConfig("zk")).build();
    }

    protected KafkaMessageReceiver<String> getReceiver(final String vertxTopic, final EventBus eventBus) {
        return msg -> {
            if (msg != null) {
                LOGGER.info("Consumed msg: " + msg);
                eventBus.send(vertxTopic, new JsonObject().putString("key", msg));
            }
        };
    }
}
