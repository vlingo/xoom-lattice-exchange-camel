package com.github.kmruiz.lattice.camel.channel;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.impl.engine.DefaultConsumerTemplate;

import java.util.Optional;
import java.util.stream.Stream;

public class ExchangeChannel<T> implements AutoCloseable {
    private final String endpoint;
    private final ConsumerTemplate consumerTemplate;
    private final Class<T> messageClass;

    public ExchangeChannel(String endpoint, CamelContext camelContext, Class<T> messageClass) {
        this.endpoint = endpoint;
        this.consumerTemplate = new DefaultConsumerTemplate(camelContext);
        this.messageClass = messageClass;
        this.consumerTemplate.start();
    }

    public Optional<T> receive() {
        return null;
    }

    public Optional<T> receive(long timeout) {
        return Optional.ofNullable(consumerTemplate.receiveBody(endpoint, timeout, messageClass));
    }

    public Stream<T> receiveBatch(int count, long timeout) {
        return null;
    }

    public Stream<T> receiveBatch(int count) {
        return null;
    }

    @Override
    public void close() {
        consumerTemplate.stop();
    }
}
