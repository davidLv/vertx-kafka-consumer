package com.md.vertx;

import com.md.vertx.kafka.KafkaConsumer;
import com.md.vertx.util.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KafkaMessageConsumerTest {

    @Mock
    private KafkaConsumer kafkaConsumer;

    @InjectMocks
    private KafkaMessageConsumer kafkaMessageConsumer;

    @Test
    public void start_shouldStartConsumer() {
        // given
        KafkaMessageConsumer kafkaMessageProcessorSpy = spy(kafkaMessageConsumer);
        doReturn(prepareDummyConfig()).when(kafkaMessageProcessorSpy).prepareConfig();
        doReturn(kafkaConsumer).when(kafkaMessageProcessorSpy).prepareKafkaConsumer(prepareDummyConfig());

        // when
        kafkaMessageProcessorSpy.start();

        // then
        verify(kafkaConsumer, times(1)).consume();
    }

    private Configuration prepareDummyConfig() {
        return Configuration.builder().groupId("groupId")
                .kafkaTopic("kafkaTopic")
                .vertxTopic("vertxTopic")
                .zkConnect("zk").build();
    }
}