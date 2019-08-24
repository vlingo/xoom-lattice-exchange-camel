package com.github.kmruiz.lattice.camel.e2e;

import com.github.kmruiz.lattice.camel.CamelTest;
import com.github.kmruiz.lattice.camel.channel.ExchangeChannel;
import com.github.kmruiz.lattice.camel.channel.ExchangeChannels;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExchangeChannelTest extends CamelTest {
    private final String ENDPOINT = "seda:" + UUID.randomUUID().toString();

    @Test
    void shouldConsumeAMessageThatIsWaitingInTheChannel() {
        ExchangeChannel<String> channel = ExchangeChannels.receivingFrom(ENDPOINT, context(), String.class);
        String[] messages = producedMessagesAre(UUID.randomUUID().toString());

        Optional<String> receivedBody = channel.receive(DEFAULT_TIMEOUT);
        assertTrue(receivedBody.isPresent());
        assertEquals(messages[0], receivedBody.get());
    }

    private String[] producedMessagesAre(String... messages) {
        Stream.of(messages).forEach(message -> producer().sendBody(ENDPOINT, message));
        return messages;
    }
}
