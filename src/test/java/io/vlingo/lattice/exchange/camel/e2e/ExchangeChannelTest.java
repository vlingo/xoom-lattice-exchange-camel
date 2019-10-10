// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.e2e;

import io.vlingo.lattice.exchange.camel.CamelTest;
import io.vlingo.lattice.exchange.camel.channel.ExchangeChannel;
import io.vlingo.lattice.exchange.camel.channel.ExchangeChannels;
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
