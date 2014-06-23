package com.md.vertx;

import com.google.inject.AbstractModule;
import com.md.vertx.deserializer.Serializer;
import com.md.vertx.deserializer.StringSerializer;
import com.md.vertx.util.Configuration;

/**
 * User: mdyminski
 */
public class VertxKafkaConsumerModule extends AbstractModule {

    private final Configuration config;

    public VertxKafkaConsumerModule(Configuration config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(Serializer.class).to(StringSerializer.class);
    }
}
