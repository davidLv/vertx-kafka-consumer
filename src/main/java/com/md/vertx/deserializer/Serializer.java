package com.md.vertx.deserializer;

/**
 * User: mdyminski
 */
public interface Serializer<T> {

    T deserialize(byte[] message);

    byte[] serialize(T object);
}
