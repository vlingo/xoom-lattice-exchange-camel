package com.github.kmruiz.lattice.camel;

import com.github.kmruiz.lattice.camel.channel.ExchangeChannel;
import io.vlingo.lattice.exchange.Exchange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class CamelTestWithDockerIntegration<T extends GenericContainer> extends CamelTest {
    private T container;
    private String content;

    protected abstract T testContainer();
    protected abstract String exchangeUri(T forContainer);

    @BeforeEach
    public void initializeContainer() {
        container = testContainer();
        container.start();

        content = UUID.randomUUID().toString();
    }

    @AfterEach
    public void tearDownContainer() {
        container.stop();
    }

    @Test
    public void shouldConsumeAndReadTheSameMessageFromTheSameExchange() throws Exception {
        String exchangeUri = exchangeUri(container);
        Exchange exchange = CamelExchange.<String>builder().camelContext(context()).endpoint(exchangeUri).name(exchangeUri).exchangeType(String.class).build();

        ExchangeChannel<String> channel = exchange.channel();

        exchange.send(content);

        Optional<String> receivedBody = channel.receive(DEFAULT_TIMEOUT);
        assertTrue(receivedBody.isPresent());
        assertEquals(content, receivedBody.get());
    }
}
