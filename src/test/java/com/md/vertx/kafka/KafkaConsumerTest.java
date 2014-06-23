package com.md.vertx.kafka;

import com.google.common.collect.Lists;
import com.md.vertx.deserializer.StringSerializer;
import kafka.consumer.KafkaStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KafkaConsumerTest {

    @Mock
    private KafkaMessageReceiver<String> receiver;

    @Mock
    private ExecutorService executor;

    @Mock
    private KafkaStream<byte[], byte[]> stream;

    @Mock
    private ConsumerThread<String> consumerThread;

    private KafkaConsumer<String> consumer;

    @Test
    public void constructor_shouldConstructObjectAndLaunchExecutors() {
        //given
        List<KafkaStream<byte[], byte[]>> streams = Lists.newArrayList();
        streams.add(stream);
        consumer = new TestableKafkaConsumer(receiver, streams, new StringSerializer(), executor);

        // when
        consumer.consume();

        // then
        verify(executor).execute(consumerThread);
    }

    private class TestableKafkaConsumer extends KafkaConsumer<String> {

        public TestableKafkaConsumer(KafkaMessageReceiver<String> messageReceiver, List<KafkaStream<byte[], byte[]>> kafkaStreams,
                                     StringSerializer deserializer, ExecutorService executor) {
            super(messageReceiver, kafkaStreams, deserializer, executor);
        }

        @Override
        protected ConsumerThread<String> getConsumerThread(KafkaMessageReceiver<String> messageReceiver,
                                                           KafkaStream<byte[], byte[]> stream) {
            return consumerThread;
        }
    }
}
