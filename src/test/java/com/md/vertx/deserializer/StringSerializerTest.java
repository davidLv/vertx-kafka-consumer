package com.md.vertx.deserializer;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class StringSerializerTest {

    private StringSerializer serializer = new StringSerializer();

    @Test
    public void serialize_shouldSerializeAndDeserialize() {
        // given
        String toSerialize = "hello";

        // when
        byte[] serialized = serializer.serialize(toSerialize);

        // then
        assertThat(serialized.length, is(5));
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