package com.md.vertx.deserializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * User: mdyminski
 */
public class KryoSerializer<T> implements Serializer<T> {

    private final Class<T> clazz;

    private static final int KRYO_BUFFER_SIZE = 204800;
    private static final int NO_MAXIMUM_BUFFER_SIZE = -1;

    public KryoSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(byte[] message) {
        try {
            Input input = new Input(message);
            T obj = KRYO.get().readObject(input, clazz);
            input.close();

            return obj;
        } catch (Exception e) {
            throw new SerializerException("Cannot deserialize string from Kafka message!", e);
        }
    }

    @Override
    public byte[] serialize(Object toSerialize) {
        try {
            Output output = new Output(KRYO_BUFFER_SIZE, NO_MAXIMUM_BUFFER_SIZE);
            KRYO.get().writeObject(output, toSerialize);
            output.close();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializerException("Cannot serialize string from Kafka message!", e);
        }
    }

    private static final ThreadLocal<Kryo> KRYO = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return new Kryo();
        }
    };
}
