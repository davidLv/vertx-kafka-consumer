package com.md.vertx.kafka;

/**
 * User: mdyminski
 */
public interface KafkaMessageReceiver<T> {
	void receive(T msg);
}
