package com.md.vertx.kafka;

import com.md.vertx.deserializer.StringSerializer;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerThreadTest {

    @Mock
    private KafkaStream<byte[], byte[]> stream;

    @Mock
    private KafkaMessageReceiver<String> receiver;

    @Mock
    private ConsumerIterator<byte[], byte[]> it;

    @Mock
    private MessageAndMetadata<byte[], byte[]> msgMetaData;

    @Test
    public void shouldReceiveMessage() {
        // given
        given(msgMetaData.message()).willReturn("Message".getBytes());
        given(stream.iterator()).willReturn(it);
        given(it.hasNext()).willReturn(true, false);
        given(it.next()).willReturn(msgMetaData);

        ConsumerThread<String> testObj = new ConsumerThread(receiver, stream, new StringSerializer());

        // when
        testObj.run();

        // then
        verify(receiver).receive("Message");
    }
}