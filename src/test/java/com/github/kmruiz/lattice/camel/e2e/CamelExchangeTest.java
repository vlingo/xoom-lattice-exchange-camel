package com.github.kmruiz.lattice.camel.e2e;

import com.github.kmruiz.lattice.camel.CamelExchange;
import com.github.kmruiz.lattice.camel.CamelTest;
import com.github.kmruiz.lattice.camel.channel.ExchangeChannel;
import io.vlingo.lattice.exchange.Exchange;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CamelExchangeTest extends CamelTest {
    private final String NAME = UUID.randomUUID().toString();
    private final String ENDPOINT = "seda:" + UUID.randomUUID().toString();
    private final String CONTENT = UUID.randomUUID().toString();

    @Test
    void shouldBeAbleToConsumeAProducerMessageFromAExchange() throws Exception {
        Exchange exchange = CamelExchange.<String>builder().camelContext(context()).endpoint(ENDPOINT).name(NAME).exchangeType(String.class).build();

        ExchangeChannel<String> channel = exchange.channel();

        exchange.send(CONTENT);

        Optional<String> receivedBody = channel.receive(DEFAULT_TIMEOUT);
        assertTrue(receivedBody.isPresent());
        assertEquals(CONTENT, receivedBody.get());
    }
}
