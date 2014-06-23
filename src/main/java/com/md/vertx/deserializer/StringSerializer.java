package com.md.vertx.deserializer;

import java.io.UnsupportedEncodingException;

/**
 * User: mdyminski
 */
public class StringSerializer implements Serializer<String> {

    @Override
    public String deserialize(byte[] message) {
        try {
            String value = new String(message, "UTF-8");
            return value;
        } catch (Exception e) {
            throw new SerializerException("Cannot deserialize string from Kafka message!", e);
        }
    }

    @Override
    public byte[] serialize(String object) {
        try {
            return object.getBytes();
        } catch (Exception e) {
            throw new SerializerException("Cannot serialize string from Kafka message!", e);
        }
    }
}
