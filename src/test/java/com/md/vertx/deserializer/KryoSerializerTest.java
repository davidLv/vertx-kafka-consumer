package com.md.vertx.deserializer;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class KryoSerializerTest {

    private KryoSerializer<String> serializer = new KryoSerializer(String.class);

    @Test
    public void serialize_shouldSerializeAndDeserialize() {
        // given
        String toSerialize = "hello";
        byte[] serialized = serializer.serialize(toSerialize);

        // when
        assertThat(serialized.length, is(6));

        // then
        String deserialized = serializer.deserialize(serialized);
        assertThat(deserialized, is(equalTo(toSerialize)));
    }

    @Test(expected = SerializerException.class)
    public void serialize_shouldThrowSerializerException() {
        // when... then exception
        serializer.serialize(null);
    }

    @Test(expected = SerializerException.class)
    public void deserialize_shouldThrowSerializerException() {
        // when... then exception
        serializer.deserialize(null);
    }
}